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
import aschi2403.tsiy.model.GeneralActivity
import aschi2403.tsiy.model.PowerActivity
import aschi2403.tsiy.model.relations.IActivity
import aschi2403.tsiy.repository.WorkoutRepo
import aschi2403.tsiy.screens.activities.MainActivity
import kotlinx.android.synthetic.main.table_row.view.*
import org.osmdroid.api.IMapController
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.BoundingBox
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.Polyline
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.math.roundToLong


private const val MAP_ZOOM = 15.0
private const val HUNDRED_DOT_ZERO = 100.0
private const val HUNDRED = 100
private const val TEN = 10

class ViewFinishedActivityFragment : Fragment() {

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

        val idOfActivity = arguments?.getLong("id")
        val powerActivity = arguments?.getBoolean("type")

        val database = activity?.let { WorkoutRepo(it) }!!
        val iActivity = if (powerActivity!!) {
            database.powerActivityById(idOfActivity!!)
        } else {
            database.generalActivityById(idOfActivity!!)
        }

        configureDeleteButton(powerActivity, database, iActivity)

        setGuiData(iActivity, powerActivity, database, idOfActivity)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    private fun setGuiData(
        iActivity: IActivity,
        powerActivity: Boolean,
        database: WorkoutRepo,
        idOfActivity: Long
    ) {
        val sdf = SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.GERMAN)
        binding.startDate.text = sdf.format(iActivity.startDate)
        binding.endDate.text = sdf.format(iActivity.endDate)

        binding.duration.text = "${durationLeadingZero(iActivity.duration)}${
            DateUtils.formatElapsedTime(
                TimeUnit.MILLISECONDS.toSeconds(
                    iActivity.duration
                )
            )
        }"

        val pauseDuration =
            TimeUnit.MILLISECONDS.toSeconds(iActivity.endDate - iActivity.startDate - iActivity.duration)
        binding.pause.text = "${durationLeadingZero(pauseDuration)}${DateUtils.formatElapsedTime(pauseDuration)}"

        binding.cardioPoints.text = iActivity.cardioPoints.toString()
        binding.caloriesValue.text = iActivity.calories.toString()

        binding.activity.text = if (powerActivity) {
            database.powerActivityTypeById(iActivity.activityTypeId).name
        } else {
            database.activityTypeById(iActivity.activityTypeId).name
        }

        setActivitySpecificInformation(powerActivity, database, idOfActivity, iActivity)

        createMap(database, idOfActivity)
    }

    @SuppressLint("SetTextI18n")
    private fun setActivitySpecificInformation(
        powerActivity: Boolean,
        database: WorkoutRepo,
        idOfActivity: Long,
        iActivity: IActivity
    ) {
        if (powerActivity) {
            val sets = database.getSetEntriesByPowerActivityId(idOfActivity).toTypedArray()
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
                (((iActivity as GeneralActivity).distance * HUNDRED).roundToLong() /
                        HUNDRED_DOT_ZERO)
            } km "
            if (iActivity.distance > 0) {
                binding.speedValue.text = "${
                    (((iActivity.distance /
                            (TimeUnit.MILLISECONDS.toHours(iActivity.duration))) * HUNDRED_DOT_ZERO)
                        .roundToLong() / HUNDRED_DOT_ZERO)
                } km/h "
            } else {
                binding.speedValue.text = R.string.zeroKmH.toString()
            }
        }
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

    private fun configureDeleteButton(
        powerActivity: Boolean,
        database: WorkoutRepo,
        iActivity: IActivity
    ) {
        val deleteButton = requireActivity().findViewById<Button>(R.id.deleteButtonAppBar)
        deleteButton.visibility = View.VISIBLE
        deleteButton.setOnClickListener {
            if (powerActivity) {
                database.deletePowerActivity(iActivity as PowerActivity)
            } else {
                database.deleteGeneralActivity(iActivity as GeneralActivity)
            }
            findNavController().popBackStack()
        }
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
