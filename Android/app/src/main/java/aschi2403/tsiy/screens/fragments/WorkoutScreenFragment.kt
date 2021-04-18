package aschi2403.tsiy.screens.fragments

import android.os.Bundle
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import aschi2403.tsiy.R
import aschi2403.tsiy.databinding.FragmentWorkoutScreenBinding
import aschi2403.tsiy.model.GeneralActivity
import aschi2403.tsiy.model.PowerActivity
import aschi2403.tsiy.repository.WorkoutRepo
import aschi2403.tsiy.viewmodel.WorkoutScreenViewModel


class WorkoutScreenFragment : Fragment() {
    private lateinit var database: WorkoutRepo
    private var idOfActivity: Long = -1
    private var set = 0
    private lateinit var binding: FragmentWorkoutScreenBinding

    private var viewModel = WorkoutScreenViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_workout_screen, container, false
        )

        database = WorkoutRepo(requireActivity().applicationContext)

        val workoutId = arguments?.getInt("workoutId", -1)!!

        if (workoutId >= 0) {
            val activities =
                database.workoutEntriesByWorkoutPlanId(workoutId.toLong()).toMutableList()

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

            if (!activities[set].isPowerActivity && set + 1 < activities.size) {
                binding.next.visibility = View.VISIBLE
                binding.next.setOnClickListener {

                    // finish activity
                    saveInDatabase(
                        activities[set].iActivityTypeId,
                        activities[set].isPowerActivity,
                        isFinished = false,
                        navigate = false
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
                arguments?.getBoolean("type")!!,
                activityTypeId,
                isWorkout = false
            )

            closeButton(activityTypeId, arguments?.getBoolean("type")!!)
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
            saveInDatabase(activityTypeId, isPowerActivity, isFinished = true, navigate = true)
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
                            activityTypeId = activityTypeId
                        )
                    )
            }
        } else {
            if (idOfActivity == -1L) {
                idOfActivity = database.addPowerActivity(
                    PowerActivity(
                        startDate = System.currentTimeMillis(),
                        activityTypeId = activityTypeId
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
                        navigate = false
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
        navigate: Boolean
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
                    endDate = System.currentTimeMillis()
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
                    endDate = System.currentTimeMillis()
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
}

