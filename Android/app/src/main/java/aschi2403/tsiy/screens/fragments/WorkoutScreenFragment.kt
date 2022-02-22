package aschi2403.tsiy.screens.fragments

import android.content.Intent
import android.content.IntentFilter
import android.os.Build
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
import aschi2403.tsiy.gps.LocationForceGroundService
import aschi2403.tsiy.helper.DialogView
import aschi2403.tsiy.helper.GPSReceiver
import aschi2403.tsiy.model.*
import aschi2403.tsiy.repository.WorkoutRepo
import aschi2403.tsiy.viewmodel.WorkoutScreenViewModel
import org.osmdroid.api.IMapController
import org.osmdroid.tileprovider.tilesource.TileSourceFactory

private const val MAP_ZOOM = 15.0

class WorkoutScreenFragment : Fragment() {
    private var isPowerActivity: Boolean = false
    private var locationService: Intent? = null
    private var workoutPlanId: Long = -1
    private var newWorkoutIdForDatabase: Long = -1
    private lateinit var repo: WorkoutRepo
    private var idOfActivity: Long = -1
    private var workoutEntryIndex = 0
    private lateinit var binding: FragmentWorkoutScreenBinding
    private lateinit var dialogView: DialogView

    private lateinit var receiver: GPSReceiver

    private var viewModel = WorkoutScreenViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                dialogView.showYesNoDialog(
                    getString(R.string.attention),
                    getString(R.string.goBackMessage)
                ) { _, _ ->
                    activity?.finish()
                }
            }
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_workout_screen, container, false
        )
        dialogView = DialogView(requireContext())

        repo = WorkoutRepo(requireActivity().applicationContext)

        workoutPlanId = arguments?.getLong("workoutPlanId") ?: -1

        if (workoutPlanId >= 0) {
            handleWorkout()
        } else {
            handleActivity()
        }

        startTimer()

        binding.pause.setOnClickListener {
            findNavController().navigate(
                WorkoutScreenFragmentDirections.actionWorkoutScreenToPauseScreen()
            )
        }
        return binding.root
    }

    private fun handleActivity() {
        val activityTypeId = arguments?.getLong("activityTypeId")!!
        isPowerActivity = repo.isPowerActivity(activityTypeId)

        if (!isPowerActivity) {
            showAndConfigureMapForNormalActivity()
        }

        binding.activity.text = arguments?.getString("name")

        if (viewModel.startDate == 0L) {
            viewModel.startDate = System.currentTimeMillis()
        }
        createActivityInDb(
            activityTypeId
        )

        configureNextButton(isPowerActivity, isWorkout = false, activityTypeId = idOfActivity)

        closeButton(activityTypeId, false)
    }

    private fun handleWorkout() {
        val data = repo.allWorkoutSessions()
        if (data.isEmpty()) {
            newWorkoutIdForDatabase = 0
        }
        if (newWorkoutIdForDatabase == -1L) {
            newWorkoutIdForDatabase = data
                .maxOf { workoutSession -> workoutSession.idForMerging }.plus(1)
        }
        val activities =
            repo.workoutEntriesByWorkoutPlanId(workoutPlanId).toMutableList()

        isPowerActivity =
            repo.allActivityTypeById(activities[workoutEntryIndex].iActivityTypeId).isPowerActivity

        closeButton(activities[workoutEntryIndex].iActivityTypeId, true)

        val upNext = if (workoutEntryIndex + 1 < activities.size) {
            repo.allActivityTypeById(
                activities[workoutEntryIndex + 1].iActivityTypeId
            ).name
        } else {
            ""
        }

        if (!isPowerActivity) {
            showAndConfigureMapForNormalActivity()
        }

        idOfActivity = viewModel.donePowerActivities.getOrDefault(activities[workoutEntryIndex].iActivityTypeId, -1)

        createActivityInDb(
            activities[workoutEntryIndex].iActivityTypeId
        )

        viewModel.startDate = System.currentTimeMillis()

        configureNextButton(
            isPowerActivity,
            upNext = upNext,
            isWorkout = true,
            activities[workoutEntryIndex].iActivityTypeId
        )

        binding.activity.text =
            repo.allActivityTypeById(activities[workoutEntryIndex].iActivityTypeId).name

        if (workoutEntryIndex + 1 == activities.size) {
            binding.next.visibility = View.INVISIBLE
        }

        configureNextButtonForCardioActivities(activities, upNext)
    }

    private fun configureNextButtonForCardioActivities(
        activities: MutableList<WorkoutPlanEntry>,
        upNext: String
    ) {
        if (!isPowerActivity && workoutEntryIndex + 1 < activities.size) {
            binding.next.visibility = View.VISIBLE
            binding.next.setOnClickListener {

                // finish activity
                saveInDatabase(
                    activities[workoutEntryIndex].iActivityTypeId,
                    isPowerActivity
                )

                if (!viewModel.donePowerActivities.containsKey(activities[workoutEntryIndex].iActivityTypeId)) {
                    repo.insertWorkoutSession(
                        WorkoutSession(
                            idOfActivity = idOfActivity,
                            idOfWorkoutPlan = workoutPlanId,
                            idForMerging = newWorkoutIdForDatabase
                        )
                    )
                }

                if (isPowerActivity) {
                    viewModel.donePowerActivities[activities[workoutEntryIndex].iActivityTypeId] = idOfActivity
                }
                // remove id of activity
                idOfActivity = -1
                viewModel.values.putLong("stopTime", -1)

                workoutEntryIndex++

                findNavController().navigate(
                    WorkoutScreenFragmentDirections.actionWorkoutScreenToPauseScreen(
                        upNext
                    )
                )
            }
        }
    }

    private fun showAndConfigureMapForNormalActivity() {
        val mapController: IMapController = binding.map.controller
        mapController.setZoom(MAP_ZOOM)
        receiver = GPSReceiver(binding.map, binding.kmValue, binding.speedValue)
        requireActivity().registerReceiver(receiver, IntentFilter("GPS_Data"))
        locationService = Intent(
            requireActivity(),
            LocationForceGroundService::class.java
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            requireActivity().startForegroundService(locationService)
        } else {
            requireActivity().startService(locationService)
        }

        binding.map.visibility = View.VISIBLE
        binding.map.setTileSource(TileSourceFactory.MAPNIK)

        binding.generalActivityHeader.visibility = View.VISIBLE
        binding.generalActivityBody.visibility = View.VISIBLE
    }

    private fun closeButton(activityTypeId: Long, isWorkout: Boolean) {
        binding.close.setOnClickListener {
            if (isWorkout && !viewModel.donePowerActivities.containsKey(activityTypeId)) {
                repo.insertWorkoutSession(
                    WorkoutSession(
                        idOfActivity = idOfActivity,
                        idOfWorkoutPlan = workoutPlanId,
                        idForMerging = newWorkoutIdForDatabase
                    )
                )
            }

            saveInDatabase(
                activityTypeId,
                isPowerActivity
            )
            if (isPowerActivity) {
                findNavController().navigate(
                    WorkoutScreenFragmentDirections.actionWorkoutScreenToChoosePowerActivityType(
                        idOfPowerActivity = idOfActivity, finished = true, upNext = null
                    )
                )
            } else {
                requireActivity().finish()
            }
            viewModel.values.clear()
        }
    }

    private fun startTimer() {
        binding.timer.base =
            SystemClock.elapsedRealtime() - viewModel.values.getLong("stopTime", 0)
        binding.timer.start()
    }

    private fun createActivityInDb(
        activityTypeId: Long
    ) {
        if (idOfActivity != -1L) {
            return
        }

        idOfActivity = repo.addActivity(
            Activity(
                startDate = System.currentTimeMillis(),
                activityTypeId = activityTypeId
            )
        )
    }

    private fun configureNextButton(
        isPowerActivity: Boolean,
        upNext: String = "",
        isWorkout: Boolean,
        activityTypeId: Long
    ) {
        if (!isPowerActivity) {
            binding.next.visibility = View.INVISIBLE
            return
        }
        binding.next.setOnClickListener {
            Navigation.findNavController(requireActivity(), R.id.fragNavWorkoutHost)
                .navigate(
                    WorkoutScreenFragmentDirections.actionWorkoutScreenToChoosePowerActivityType(
                        idOfPowerActivity = idOfActivity, finished = false, upNext = upNext
                    )
                )
            if (isWorkout) {
                if (!viewModel.donePowerActivities.containsKey(activityTypeId)) {
                    repo.insertWorkoutSession(
                        WorkoutSession(
                            idOfActivity = idOfActivity,
                            idOfWorkoutPlan = workoutPlanId,
                            idForMerging = newWorkoutIdForDatabase
                        )
                    )
                }

                // finish activity
                saveInDatabase(
                    activityTypeId,
                    isPowerActivity
                )

                if (isPowerActivity) {
                    viewModel.donePowerActivities[activityTypeId] = idOfActivity
                }
                // remove id of activity
                idOfActivity = -1
                viewModel.values.putLong("stopTime", -1)
            }

            workoutEntryIndex++
        }
    }

    private fun saveInDatabase(
        activityTypeId: Long,
        isPowerActivity: Boolean
    ) {
        val duration = repo.getActivityById(idOfActivity).duration + SystemClock.elapsedRealtime() - binding.timer.base
        repo.updateActivity(
            Activity(
                id = idOfActivity,
                activityTypeId = activityTypeId,
                startDate = repo.getActivityWithCardioActivityById(idOfActivity).activity.startDate,
                duration = duration,
                endDate = System.currentTimeMillis()
            )
        )
        if (!isPowerActivity) { // cardio activity
            // save additional data
            repo.addCardioActivity(
                CardioActivity(
                    id = idOfActivity,
                    distance = receiver.distance
                )
            )
            repo.insertGPSPoints(receiver.geoPoints.map {
                GPSPoint(
                    null,
                    it.latitude,
                    it.longitude,
                    idOfActivity
                )
            })

            requireActivity().unregisterReceiver(receiver)
            requireActivity().stopService(locationService)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (viewModel.values.getLong("stopTime") != -1L) {
            viewModel.values.putLong("stopTime", SystemClock.elapsedRealtime() - binding.timer.base)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (!isPowerActivity) {
            requireActivity().stopService(locationService)
        }
    }

    override fun onResume() {
        super.onResume()
        binding.map.onResume()
    }

    override fun onPause() {
        super.onPause()
        binding.map.onPause()
    }
}
