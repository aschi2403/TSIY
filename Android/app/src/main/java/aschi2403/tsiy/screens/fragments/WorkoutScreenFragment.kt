package aschi2403.tsiy.screens.fragments

import android.os.Bundle
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import aschi2403.tsiy.R
import aschi2403.tsiy.databinding.FragmentWorkoutScreenBinding
import aschi2403.tsiy.gps.LocationProvider
import aschi2403.tsiy.helper.DialogView
import aschi2403.tsiy.model.GeneralActivity
import aschi2403.tsiy.model.PowerActivity
import aschi2403.tsiy.repository.WorkoutRepo
import aschi2403.tsiy.viewmodel.WorkoutScreenViewModel


class WorkoutScreenFragment : Fragment() {
    private var workoutPlanId: Long = -1
    private var newWorkoutIdForDatabase: Int = -1
    private var isPowerActivity: Boolean = true
    private lateinit var database: WorkoutRepo
    private var idOfActivity: Long = -1
    private var set = 0
    private lateinit var binding: FragmentWorkoutScreenBinding
    private lateinit var locationProvider: LocationProvider
    private lateinit var dialogView: DialogView

    private var viewModel = WorkoutScreenViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                dialogView.showYesNoDialog(
                    getString(R.string.attention),
                    getString(R.string.goBackMessage),
                    { _, _ ->
                        activity?.finish()
                    },
                    { _, _ -> }
                )
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_workout_screen, container, false
        )
        dialogView = DialogView(requireContext())
        isPowerActivity = arguments?.getBoolean("type")!!

        if (!isPowerActivity) {
            binding.generalActivityHeader.visibility = View.VISIBLE
            binding.generalActivityBody.visibility = View.VISIBLE
            locationProvider = LocationProvider(binding.kmValue, binding.speedValue)
            locationProvider.getLastKnownLocation(requireContext())
        }

        database = WorkoutRepo(requireActivity().applicationContext)

        workoutPlanId = arguments?.getLong("workoutPlanId", -1)!!

        if (workoutPlanId >= 0) {
            val data = database.allGeneralActivities.plus(database.allPowerActivities)
            if (data.isNotEmpty() && newWorkoutIdForDatabase == -1) {
                newWorkoutIdForDatabase = data
                    .maxOf { activity -> activity.workoutId }.plus(1)
            }
            val activities =
                database.workoutEntriesByWorkoutPlanId(workoutPlanId).toMutableList()

            closeButton(activities[set].iActivityTypeId, activities[set].isPowerActivity)

            createActivityInDb(
                activities[set].isPowerActivity,
                activities[set].iActivityTypeId,
                isWorkout = true
            )

            changeActivityNameLabel(
                activities[set].isPowerActivity,
                activities[set].iActivityTypeId
            )

            if (set + 1 == activities.size) {
                binding.next.visibility = View.INVISIBLE
            }

            if (!activities[set].isPowerActivity && set + 1 < activities.size) {
                binding.next.visibility = View.VISIBLE
                binding.next.setOnClickListener {

                    // finish activity
                    saveInDatabase(
                        activities[set].iActivityTypeId,
                        activities[set].isPowerActivity,
                        isFinished = false,
                        navigate = false,
                        newWorkoutIdForDatabase = newWorkoutIdForDatabase,
                        workoutPlanId = workoutPlanId
                    )
                    // remove id of activity
                    idOfActivity = -1
                    viewModel.values.putLong("stopTime", -1)

                    set++

                    findNavController().navigate(
                        WorkoutScreenFragmentDirections.actionWorkoutScreenToPauseScreen(
                        )
                    )
                }
            }
        } else {
            val activityTypeId = arguments?.getLong("activityTypeId")!!

            binding.activity.text = arguments?.getString("name")

            createActivityInDb(
                isPowerActivity,
                activityTypeId,
                isWorkout = false
            )

            closeButton(activityTypeId, isPowerActivity)
        }

        startTimer()

        binding.pause.setOnClickListener {
            findNavController().navigate(
                WorkoutScreenFragmentDirections.actionWorkoutScreenToPauseScreen(
                )
            )
        }
        return binding.root
    }

    private fun closeButton(activityTypeId: Long, isPowerActivity: Boolean) {
        binding.close.setOnClickListener {
            saveInDatabase(
                activityTypeId,
                isPowerActivity,
                isFinished = true,
                navigate = true,
                newWorkoutIdForDatabase = newWorkoutIdForDatabase,
                workoutPlanId = workoutPlanId
            )
            viewModel.values.clear()
        }
    }

    private fun changeActivityNameLabel(isPowerActivity: Boolean, iActivityTypeId: Long) {
        if (isPowerActivity) {
            binding.activity.text =
                database.powerActivityTypeById(iActivityTypeId).name
        } else {
            binding.activity.text =
                database.activityTypeById(iActivityTypeId).name
        }
    }

    private fun startTimer() {
        binding.timer.base =
            SystemClock.elapsedRealtime() - viewModel.values.getLong("stopTime", 0)
        binding.timer.start()
    }

    private fun createActivityInDb(
        isPowerActivity: Boolean,
        activityTypeId: Long,
        isWorkout: Boolean
    ) {
        if (!isPowerActivity) { //normal activity
            binding.next.visibility = View.INVISIBLE
            if (idOfActivity == -1L) {
                idOfActivity =
                    database.addGeneralActivity(
                        GeneralActivity(
                            startDate = System.currentTimeMillis(),
                            activityTypeId = activityTypeId,
                            workoutId = -1
                        )
                    )
            }
        } else {
            if (idOfActivity == -1L) {
                idOfActivity = database.addPowerActivity(
                    PowerActivity(
                        startDate = System.currentTimeMillis(),
                        activityTypeId = activityTypeId,
                        workoutId = -1
                    )
                )
            }
            binding.next.setOnClickListener {
                Navigation.findNavController(requireActivity(), R.id.fragNavWorkoutHost)
                    .navigate(
                        WorkoutScreenFragmentDirections.actionWorkoutScreenToChoosePowerActivityType(
                            idOfPowerActivity = idOfActivity, finished = false
                        )
                    )
                if (isWorkout) {
                    // finish activity
                    saveInDatabase(
                        activityTypeId,
                        isPowerActivity,
                        isFinished = false,
                        navigate = false,
                        newWorkoutIdForDatabase = newWorkoutIdForDatabase,
                        workoutPlanId = workoutPlanId
                    )
                    // remove id of activity
                    idOfActivity = -1
                    viewModel.values.putLong("stopTime", -1)
                }

                set++

            }
        }
    }

    private fun saveInDatabase(
        activityTypeId: Long,
        isPowerActivity: Boolean,
        isFinished: Boolean,
        navigate: Boolean,
        newWorkoutIdForDatabase: Int,
        workoutPlanId: Long
    ) {
        if (!isPowerActivity) { // normal activity
            database.updateActivity(
                GeneralActivity(
                    id = idOfActivity,
                    activityTypeId = activityTypeId,
                    startDate = database.generalActivityById(idOfActivity).startDate,
                    duration = SystemClock.elapsedRealtime() - binding.timer.base,
                    calories = 0.0,
                    cardioPoints = 0.0,
                    endDate = System.currentTimeMillis(),
                    distance = locationProvider.getDistance(),
                    workoutId = newWorkoutIdForDatabase,
                    workoutPlanId = workoutPlanId
                )
            ) //TODO: calculate cardioPoints and calories

            if (isFinished) {
                activity?.finish()
            }
        } else { // power activity
            database.updatePowerActivity(
                PowerActivity(
                    id = idOfActivity,
                    activityTypeId = activityTypeId,
                    startDate = database.powerActivityById(idOfActivity).startDate,
                    duration = SystemClock.elapsedRealtime() - binding.timer.base,
                    calories = 0.0,
                    cardioPoints = 0.0,
                    sets = set,
                    endDate = System.currentTimeMillis(),
                    workoutId = newWorkoutIdForDatabase,
                    workoutPlanId = workoutPlanId
                )
            )
            if (navigate)
                findNavController().navigate(
                    WorkoutScreenFragmentDirections.actionWorkoutScreenToChoosePowerActivityType(
                        idOfPowerActivity = idOfActivity, finished = isFinished
                    )
                )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (viewModel.values.getLong("stopTime") != -1L)
            viewModel.values.putLong("stopTime", SystemClock.elapsedRealtime() - binding.timer.base)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (!isPowerActivity) {
            locationProvider.stopLocation()
        }
    }
}

