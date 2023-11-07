package unit.chip.libchip.common

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment


/**
 * Created by DinhTC on 11/3/2023.
 * Skype: 0975469232
 */


object Utils {

    fun checkPermission(fragment: Fragment, permissionString: String, permissionCode: Int) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M || fragment.context == null) return
        val existingPermissionStatus = ContextCompat.checkSelfPermission(
            fragment.requireContext(),
            permissionString
        )
        if (existingPermissionStatus == PackageManager.PERMISSION_GRANTED) return
        fragment.requestPermissions(arrayOf(permissionString), permissionCode)
    }

    fun isCameraGranted(context: Context?): Boolean {
        val cameraPermissionGranted = ContextCompat.checkSelfPermission(
            context!!,
            Manifest.permission.CAMERA
        )
        return cameraPermissionGranted == PackageManager.PERMISSION_GRANTED
    }
}