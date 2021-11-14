package aschi2403.tsiy.screens.fragments

import android.Manifest
import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import aschi2403.tsiy.R
import aschi2403.tsiy.databinding.FragmentWorkoutScreenBinding
import aschi2403.tsiy.gps.LocationForceGroundService
import aschi2403.tsiy.helper.DialogView
import aschi2403.tsiy.model.GPSPoint
import aschi2403.tsiy.model.GeneralActivity
import aschi2403.tsiy.model.PowerActivity
import aschi2403.tsiy.repository.WorkoutRepo
import aschi2403.tsiy.viewmodel.WorkoutScreenViewModel
import org.osmdroid.api.IMapController
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polyline


class WorkoutScreenFragment : Fragment() {
    private lateinit var locationService: Intent
    private var isPowerActivity: Boolean = true
    private lateinit var database: WorkoutRepo
    private var idOfActivity: Long = -1
    private var set = 0
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_workout_screen, container, false
        )

        requestPermissions(
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
            )
        )

        binding.map.setTileSource(TileSourceFactory.MAPNIK)
        val mapController: IMapController = binding.map.controller
        mapController.setZoom(15.0)
        dialogView = DialogView(requireContext())
        isPowerActivity = arguments?.getBoolean("isPowerActivity")!!

        if (!isPowerActivity) {
            binding.generalActivityHeader.visibility = View.VISIBLE
            binding.generalActivityBody.visibility = View.VISIBLE
        }

        database = WorkoutRepo(requireActivity().applicationContext)

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
                    endDate = System.currentTimeMillis(),
                    distance = receiver.distance
                )
            ) //TODO: calculate cardioPoints and calories
            database.insertGPSPoints(receiver.geoPoints.map {
                GPSPoint(
                    null,
                    it.latitude,
                    it.longitude,
                    idOfActivity
                )
            })

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
        requireActivity().unregisterReceiver(receiver)
        requireActivity().stopService(locationService)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (viewModel.values.getLong("stopTime") != -1L)
            viewModel.values.putLong("stopTime", SystemClock.elapsedRealtime() - binding.timer.base)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (!isPowerActivity) {
            requireActivity().stopService(locationService)
        }
    }

    private fun requestPermissions(permissions: Array<String>) {
        permissions.forEach {
            if (ContextCompat.checkSelfPermission(this.requireContext(), it)
                != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this.context as Activity,
                    arrayOf(it),
                    1
                )
            }
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

    internal class GPSReceiver(private val map: MapView, private val kmValue: TextView, private val speedValue: TextView) :
        BroadcastReceiver() {


        val geoPoints: MutableList<GeoPoint> = ArrayList()
        var distance: Float = 0F
        private val polyline = Polyline()
        private var positionMarker: Marker


        init {
            map.overlays.add(polyline)
            positionMarker = Marker(map)
            map.overlayManager.add(positionMarker)
        }

        override fun onReceive(context: Context?, intent: Intent) {
            if (intent.action == "GPS_Data") {
                val latitude = intent.getDoubleExtra("latitude", 0.0)
                val longitude = intent.getDoubleExtra("longitude", 0.0)
                distance+= intent.getFloatExtra("distance", 0F)
                val speed = intent.getFloatExtra("speed", 0F)

                val point = GeoPoint(latitude, longitude)
                positionMarker.position = point
                map.controller.setCenter(point)
                polyline.addPoint(point)

                geoPoints.add(point)
                kmValue.text = String.format("%.2f km", distance)
                speedValue.text = String.format("%.2f km/h", speed * 3.6)
            }
        }
    }
}

