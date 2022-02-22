package aschi2403.tsiy.screens.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import aschi2403.tsiy.R
import aschi2403.tsiy.databinding.FragmentViewfinishedactivityBinding
import aschi2403.tsiy.helper.ButtonHelper
import aschi2403.tsiy.model.relations.ActivityWithCardioActivity
import aschi2403.tsiy.repository.WorkoutRepo
import aschi2403.tsiy.screens.activities.MainActivity
import kotlinx.android.synthetic.main.table_row.view.weightValue
import kotlinx.android.synthetic.main.table_row.view.repetitionsValue
import org.osmdroid.api.IMapController
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.BoundingBox
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.Polyline
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.TimeUnit
import kotlin.math.roundToLong


private const val MAP_ZOOM = 15.0
private const val HUNDRED_DOT_ZERO = 100.0
private const val HUNDRED = 100
private const val TAUSEND = 1000
private const val SIXTY_DOT_ZERO = 60.0
private const val SIXTY = 60.0
private const val TEN = 10

class ViewFinishedActivityFragment : Fragment() {

    private lateinit var repo: WorkoutRepo
    private lateinit var binding: FragmentViewfinishedactivityBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_viewfinishedactivity, container, false
        )

        (requireActivity() as MainActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val idOfActivity = arguments?.getLong("id")!!

        repo = WorkoutRepo(requireActivity())
        val activity = repo.getActivityWithCardioActivityById(idOfActivity)

        ButtonHelper().configureDeleteButton(requireActivity()) {
            repo.deleteActivity(activity.activity)
            findNavController().popBackStack()
        }

        setGuiData(activity, repo, idOfActivity)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    private fun setGuiData(
        activity: ActivityWithCardioActivity,
        database: WorkoutRepo,
        idOfActivity: Long
    ) {
        val sdf = SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.GERMAN)
        binding.startDate.text = sdf.format(activity.activity.startDate)
        binding.endDate.text = sdf.format(activity.activity.endDate)

        binding.duration.text = "${durationLeadingZero(activity.activity.duration)}${
            DateUtils.formatElapsedTime(
                TimeUnit.MILLISECONDS.toSeconds(
                    activity.activity.duration
                )
            )
        }"

        val pause = activity.activity.endDate - activity.activity.startDate - activity.activity.duration
        val pauseDuration =
            TimeUnit.MILLISECONDS.toSeconds(pause)
        binding.pause.text = "${durationLeadingZero(pauseDuration)}${DateUtils.formatElapsedTime(pauseDuration)}"

        // binding.cardioPoints.text = iActivity.cardioPoints.toString()
        // binding.caloriesValue.text = iActivity.calories.toString()

        binding.activity.text = database.allActivityTypeById(activity.activityType.id!!).name

        setActivitySpecificInformation(database, activity)

        createMap(database, idOfActivity)
    }

    @SuppressLint("SetTextI18n")
    private fun setActivitySpecificInformation(
        database: WorkoutRepo,
        activity: ActivityWithCardioActivity
    ) {
        if (activity.activityType.isPowerActivity) {
            val sets = database.getSetEntriesByPowerActivityId(activity.activity.id!!).toTypedArray()
            if (sets.isNotEmpty()) {
                binding.header.visibility = View.VISIBLE
            }
            sets.forEach {
                val row = LayoutInflater.from(context).inflate(R.layout.table_row, null)
                row.repetitionsValue.text = it.repetitions.toString()
                row.weightValue.text = it.weight.toString()
                binding.sets.addView(row)
            }
        } else {
            binding.generalActivityBody.visibility = View.VISIBLE
            binding.generalActivityHeader.visibility = View.VISIBLE
            binding.map.visibility = View.VISIBLE
            binding.distanceValue.text = "${
                ((activity.cardioActivity!!.distance * HUNDRED).roundToLong() / HUNDRED_DOT_ZERO)
            } km "

            if (activity.cardioActivity!!.distance > 0.0) {
                binding.speedValue.text = "${
                    (((activity.cardioActivity!!.distance / (millisToHours(activity.activity.duration))) * HUNDRED_DOT_ZERO)
                        .roundToLong() / HUNDRED_DOT_ZERO)
                } km/h "
            } else {
                binding.speedValue.text = requireContext().getString(R.string.zeroKmH)
            }
        }
    }

    private fun millisToHours(duration: Long): Double {
        return ((duration / TAUSEND) / SIXTY_DOT_ZERO) / SIXTY
    }

    private fun createMap(database: WorkoutRepo, idOfActivity: Long) {
        binding.map.setTileSource(TileSourceFactory.MAPNIK)
        val mapController: IMapController = binding.map.controller
        mapController.setZoom(MAP_ZOOM)
        val gpsPoints = database.getGPSPointsFromActivity(idOfActivity)
        if (gpsPoints.isNotEmpty()) {
            val boundingBox =
                BoundingBox.fromGeoPoints(gpsPoints.map { gpsPoint ->
                    GeoPoint(
                        gpsPoint.latitude,
                        gpsPoint.longitude
                    )
                }.toList())
            mapController.setCenter(boundingBox.centerWithDateLine)
            mapController.zoomToSpan(
                boundingBox.latitudeSpan,
                boundingBox.longitudeSpanWithDateLine
            )
        } else {
            binding.map.visibility = View.GONE
        }
        val polyline = Polyline()

        gpsPoints.forEach {
            polyline.addPoint(GeoPoint(it.latitude, it.longitude))
        }
        binding.map.overlays.add(polyline)
        binding.map.invalidate()
    }


    private fun durationLeadingZero(duration: Long): String {
        if (TimeUnit.MILLISECONDS.toHours(duration) < 1) {
            return "00:"
        }
        if (TimeUnit.MILLISECONDS.toHours(duration) < TEN) {
            return "0"
        }
        return ""
    }

    override fun onDestroy() {
        super.onDestroy()
        requireActivity().findViewById<Button>(R.id.deleteButtonAppBar).visibility = View.GONE
    }
}
