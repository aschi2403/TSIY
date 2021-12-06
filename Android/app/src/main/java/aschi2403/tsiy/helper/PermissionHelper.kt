package aschi2403.tsiy.helper

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

const val REQUEST_CODE = 500

class PermissionHelper {
    fun askPermissions(activity: Activity, permissions: Array<String>) {
        if (!checkPermissions(activity, permissions)) {
            ActivityCompat.requestPermissions(
                activity, permissions,
                REQUEST_CODE
            )
        }
    }

    private fun checkPermissions(context: Context, permissions: Array<String>): Boolean {
        permissions.forEach {
            if (ContextCompat.checkSelfPermission(context, it) !=
                PackageManager.PERMISSION_GRANTED
            ) {
                return false
            }
        }
        return true
    }
}
