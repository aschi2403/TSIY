package aschi2403.tsiy.gps

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.annotation.RequiresApi


class LocationForceGroundService: Service() {
    private lateinit var locationProvider: LocationProvider


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate() {
        super.onCreate()

        locationProvider = LocationProvider(baseContext)
        locationProvider.startLocationTracking(baseContext)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel =
                NotificationChannel("TSIYLocationChannel", "TSIY Location Provider", NotificationManager.IMPORTANCE_LOW)
                getSystemService(NotificationManager::class.java).createNotificationChannel(serviceChannel)

            startForeground(134, Notification.Builder(baseContext, "TSIYLocationChannel").build())
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}