package com.zhy.file

import android.os.Environment
import com.google.gson.Gson
import org.json.JSONObject
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

fun File.mkdirIfExist() {
    if (exists()) return
    mkdirs()
}

internal inline fun <reified T> T.saveJsonToFile(index: Int, uiStateDir: File) {
    val uiStateIntroduce = JSONObject().apply {
        val canonicalName = if (this@saveJsonToFile!!::class.java.canonicalName.isNullOrBlank()) {
            "uiState"
        } else {
            this@saveJsonToFile!!::class.java.canonicalName
        }
        put(canonicalName, Gson().toJson(this@saveJsonToFile))
    }.toString()
    val file = File(uiStateDir, "$index.txt")
    file.writeText(uiStateIntroduce)
}
