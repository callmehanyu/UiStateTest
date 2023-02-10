package com.base.perm

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.core.app.ActivityCompat

private val PERMISSIONS_STORAGE = arrayOf<String>(
    Manifest.permission.READ_EXTERNAL_STORAGE,
    Manifest.permission.WRITE_EXTERNAL_STORAGE
)

internal fun checkPermission(activity: Activity) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        checkPermissionAfterR(activity)
    } else {
        checkPermissionBeforeR(activity)
    }
}

private fun checkPermissionBeforeR(activity: Activity) {
    try {
        val permission = ActivityCompat.checkSelfPermission(
            activity,
            "android.permission.WRITE_EXTERNAL_STORAGE"
        )
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, 100)
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

private fun checkPermissionAfterR(activity: Activity) {
    val intent = Intent()

    intent.action = Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION

    val uri: Uri = Uri.fromParts("package", activity.packageName, null)

    intent.data = uri

    activity.startActivity(intent)
}