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
import aschi2403.tsiy.model.GPSPoint
import aschi2403.tsiy.model.GeneralActivity
import aschi2403.tsiy.model.PowerActivity
import aschi2403.tsiy.repository.WorkoutRepo
import aschi2403.tsiy.viewmodel.WorkoutScreenViewModel
import org.osmdroid.api.IMapController
import org.osmdroid.tileprovider.tilesource.TileSourceFactory

private const val MAP_ZOOM = 15.0

class WorkoutScreenFragment : Fragment() {
    private var locationService: Intent? = null
    private var workoutPlanId: Long = -1
    private var newWorkoutIdForDatabase: Int = -1
    private var isPowerActivity: Boolean = true
    private lateinit var database: WorkoutRepo
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
                    getString(R.string.goBackMessage),
                    { _, _ ->
                        activity?.finish()
                    },
                    { _, _ -> }
                )
            }
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_workout_screen, container, false
        )
        dialogView = DialogView(requireContext())
        isPowerActivity = arguments?.getBoolean("type")!!

        database = WorkoutRepo(requireActivity().applicationContext)

        workoutPlanId = arguments?.getLong("workoutPlanId", -1)!!

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
        if (!isPowerActivity) {
            showAndConfigureMapForNormalActivity()
        }

        val activityTypeId = arguments?.getLong("activityTypeId")!!
        binding.activity.text = arguments?.getString("name")

        createActivityInDb(
            isPowerActivity,
            activityTypeId,
            isWorkout = false,
            upNext = null
        )

        closeButton(activityTypeId, isPowerActivity)
    }

    private fun handleWorkout() {
        val data = database.allGeneralActivities.plus(database.allPowerActivities)
        if (data.isNotEmpty() && newWorkoutIdForDatabase == -1) {
            newWorkoutIdForDatabase = data
                .maxOf { activity -> activity.workoutId }.plus(1)
        }
        val activities =
            database.workoutEntriesByWorkoutPlanId(workoutPlanId).toMutableList()

            closeButton(activities[workoutEntryIndex].iActivityTypeId, activities[workoutEntryIndex].isPowerActivity)

        val upNext = if (workoutEntryIndex + 1 < activities.size) {
            database.allActivityTypeById(
                activities[workoutEntryIndex + 1].iActivityTypeId
            ).name
        } else {
            null
        }

            if (!activities[workoutEntryIndex].isPowerActivity) {
                showAndConfigureMapForNormalActivity()
            }

            createActivityInDb(
                activities[workoutEntryIndex].isPowerActivity,
                activities[workoutEntryIndex].iActivityTypeId,
                isWorkout = true,
                upNext
            )

        binding.activity.text =
            database.allActivityTypeById(activities[workoutEntryIndex].iActivityTypeId).name

        if (workoutEntryIndex + 1 == activities.size) {
            binding.next.visibility = View.INVISIBLE
        }

        if (!activities[workoutEntryIndex].isPowerActivity && workoutEntryIndex + 1 < activities.size) {
            binding.next.visibility = View.VISIBLE
            binding.next.setOnClickListener {

                // finish activity
                saveInDatabase(
                    activities[workoutEntryIndex].iActivityTypeId,
                    activities[workoutEntryIndex].isPowerActivity,
                    newWorkoutIdForDatabase = newWorkoutIdForDatabase,
                    workoutPlanId = workoutPlanId
                )
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
        binding.map.visibility = View.VISIBLE
        binding.map.setTileSource(TileSourceFactory.MAPNIK)
        val mapController: IMapController = binding.map.controller
        mapController.setZoom(MAP_ZOOM)
        binding.generalActivityHeader.visibility = View.VISIBLE
        binding.generalActivityBody.visibility = View.VISIBLE
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
    }

    private fun closeButton(activityTypeId: Long, isPowerActivity: Boolean) {
        binding.close.setOnClickListener {
            saveInDatabase(
                activityTypeId,
                isPowerActivity,
                newWorkoutIdForDatabase = newWorkoutIdForDatabase,
                workoutPlanId = workoutPlanId
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
        isPowerActivity: Boolean,
        activityTypeId: Long,
        isWorkout: Boolean,
        upNext: String?
    ) {
        if (!isPowerActivity) { // normal activity
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
                            idOfPowerActivity = idOfActivity, finished = false, upNext = upNext
                        )
                    )
                if (isWorkout) {
                    // finish activity
                    saveInDatabase(
                        activityTypeId,
                        isPowerActivity,
                        newWorkoutIdForDatabase = newWorkoutIdForDatabase,
                        workoutPlanId = workoutPlanId
                    )
                    // remove id of activity
                    idOfActivity = -1
                    viewModel.values.putLong("stopTime", -1)
                }

                workoutEntryIndex++
            }
        }
    }

    private fun saveInDatabase(
        activityTypeId: Long,
        isPowerActivity: Boolean,
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
                    distance = receiver.distance,
                    workoutId = newWorkoutIdForDatabase,
                    workoutPlanId = workoutPlanId
                )
            ) // TODO: calculate cardioPoints and calories
            database.insertGPSPoints(receiver.geoPoints.map {
                GPSPoint(
                    null,
                    it.latitude,
                    it.longitude,
                    idOfActivity
                )
            })
        } else { // power activity
            database.updatePowerActivity(
                PowerActivity(
                    id = idOfActivity,
                    activityTypeId = activityTypeId,
                    startDate = database.powerActivityById(idOfActivity).startDate,
                    duration = SystemClock.elapsedRealtime() - binding.timer.base,
                    calories = 0.0,
                    cardioPoints = 0.0,
                    sets = workoutEntryIndex,
                    endDate = System.currentTimeMillis(),
                    workoutId = newWorkoutIdForDatabase,
                    workoutPlanId = workoutPlanId
                )
            )
        }
        if (!isPowerActivity) {
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
