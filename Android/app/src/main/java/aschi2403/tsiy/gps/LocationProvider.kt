package aschi2403.tsiy.gps

import android.Manifest
import android.content.Context
import android.content.Context.LOCATION_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContextCompat
import aschi2403.tsiy.R

const val THOUSAND = 1000L

class LocationProvider(val context: Context) {

    private lateinit var locationManager: LocationManager
    private lateinit var oldLocation: Location

    private val locationListener: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            if (!::oldLocation.isInitialized) {
                oldLocation = Location("oldLoc")
                oldLocation.latitude = location.latitude
                oldLocation.longitude = location.longitude
            }

            val gpsData = Intent()
            gpsData.action = "GPS_Data"
            gpsData.putExtra("latitude", location.latitude)
            gpsData.putExtra("longitude", location.longitude)
            gpsData.putExtra("distance", oldLocation.distanceTo(location) / THOUSAND)
            gpsData.putExtra("speed", location.speed)
            context.sendBroadcast(gpsData)


            oldLocation.latitude = location.latitude
            oldLocation.longitude = location.longitude
        }

        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {
            // noting to do
        }

        override fun onProviderEnabled(provider: String) {
            // noting to do
        }

        override fun onProviderDisabled(provider: String) {
            // noting to do
        }
    }


    fun startLocationTracking(context: Context) {
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

                locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    THOUSAND,
                    1f,
                    locationListener
                )

                if (location != null) {
                    oldLocation = Location("oldLoc")
                    oldLocation.latitude = location.latitude
                    oldLocation.longitude = location.longitude
                } else {
                    Toast.makeText(
                        context,
                        context.getString(R.string.unableToGetLocation),
                        Toast.LENGTH_LONG
                    ).show()
                }
            } else {
                Toast.makeText(
                    context,
                    context.getString(R.string.turnOnLocation),
                    Toast.LENGTH_LONG
                ).show()
            }
        } else {
            Toast.makeText(
                context,
                context.getString(R.string.locationIsNeedToRecordDistance),
                Toast.LENGTH_LONG
            ).show()
        }
    }
}
