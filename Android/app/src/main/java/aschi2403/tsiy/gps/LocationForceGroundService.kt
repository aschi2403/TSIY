package aschi2403.tsiy.gps

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.Build
import android.os.IBinder

const val ID_OF_CHANNEL = 134
class LocationForceGroundService : Service() {
    private lateinit var locationProvider: LocationProvider

    inner class LocationForceGroundServiceBinder : Binder() {
        fun getService() = this@LocationForceGroundService
    }

    override fun onCreate() {
        super.onCreate()

        locationProvider = LocationProvider(baseContext)
        locationProvider.startLocationTracking(baseContext)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel =
                NotificationChannel("TSIYLocationChannel", "TSIY Location Provider", NotificationManager.IMPORTANCE_LOW)
            getSystemService(NotificationManager::class.java).createNotificationChannel(serviceChannel)

            startForeground(ID_OF_CHANNEL, Notification.Builder(baseContext, "TSIYLocationChannel").build())
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int = START_NOT_STICKY

    override fun onBind(intent: Intent?): IBinder {
        return LocationForceGroundServiceBinder()
    }
}
