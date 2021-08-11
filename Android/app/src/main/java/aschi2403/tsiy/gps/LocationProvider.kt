package aschi2403.tsiy.gps

import android.Manifest
import android.content.Context
import android.content.Context.LOCATION_SERVICE
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat

class LocationProvider(kmValue: TextView, speedValue: TextView) {

    private lateinit var locationManager: LocationManager
    private var oldLocation: Location = Location("oldLong")
    private var distance: Float = 0F


    private val locationListener: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            distance += oldLocation.distanceTo(location) / 1000
            kmValue.text = String.format("%.2f km", distance)
            speedValue.text = String.format("%.2f km/h", location.speed * 3.6)

            oldLocation.latitude = location.latitude
            oldLocation.longitude = location.longitude
        }

        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
        override fun onProviderEnabled(provider: String) {}
        override fun onProviderDisabled(provider: String) {}
    }


    fun getLastKnownLocation(context: Context) {
        locationManager = context.getSystemService(LOCATION_SERVICE) as LocationManager
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            == PackageManager.PERMISSION_GRANTED
        ) {
            if (locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER)
            ) {
                val location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)

                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0L, 0f, locationListener)

                if (location != null) {
                    oldLocation.latitude = location.latitude
                    oldLocation.longitude = location.longitude
                }
            } else {
                Toast.makeText(context, "Please turn on location and start the activity again", Toast.LENGTH_LONG)
                    .show()
            }
        } else {
            Toast.makeText(context, "Location permission is needed to record the distance.", Toast.LENGTH_LONG).show()
        }
    }

    fun stopLocation() {
        locationManager.removeUpdates(locationListener)
    }

    fun getDistance(): Float {
        return distance
    }
}