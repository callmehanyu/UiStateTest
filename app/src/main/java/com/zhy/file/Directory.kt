package com.zhy.file

import android.os.Environment
import java.io.File

/**
 * 获取外部转码目录
 */
internal fun getUiTestDirectory(): File {
    val file = File(Environment.getExternalStorageDirectory(), "netdiskUiTest")
    return if (file.exists()) {
        if (file.isDirectory) {
            file
        } else {
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES)
        }
    } else {
        if (file.mkdirs()) {
            file
        } else {
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES)
        }
    }
}