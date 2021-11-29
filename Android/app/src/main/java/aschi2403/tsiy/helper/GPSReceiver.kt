package aschi2403.tsiy.helper

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.TextView
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polyline
import java.util.*
import kotlin.collections.ArrayList

const val M_IN_SECONDS_TO_KM_IN_HOURS = 3.6

class GPSReceiver(
    private val map: MapView,
    private val kmValue: TextView,
    private val speedValue: TextView
) :
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
            distance += intent.getFloatExtra("distance", 0F)
            val speed = intent.getFloatExtra("speed", 0F)

            val point = GeoPoint(latitude, longitude)
            positionMarker.position = point
            map.controller.setCenter(point)
            polyline.addPoint(point)

            geoPoints.add(point)
            kmValue.text = String.format(Locale.GERMAN, "%.2f km", distance)
            speedValue.text =
                String.format(Locale.GERMAN, "%.2f km/h", speed * M_IN_SECONDS_TO_KM_IN_HOURS)
        }
    }
}
