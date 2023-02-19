package com.zhy.launcher

import android.app.ActivityGroup
import android.os.Bundle
import android.os.Environment
import android.os.Environment.getExternalStorageDirectory
import android.util.Log
import com.base.applicationScope
import com.zhy.R
import com.zhy.demo.TestActivity
import com.zhy.launcher.biz.screenShotAllState
import kotlinx.coroutines.launch
import java.io.File

private const val TAG = "UiTestLauncherActivity"
const val LAUNCHER_DEBUG_ACTIVITY_START_KEY_ACTIVITY_LIST = "activity_list"

internal class UiTestLauncherActivity : ActivityGroup() {

    private val testActivityNameList: List<String> by lazy {
        intent.getStringExtra(LAUNCHER_DEBUG_ACTIVITY_START_KEY_ACTIVITY_LIST)?.split(',')
            ?: emptyList()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group_ui_test_launcher)
        com.base.perm.checkPermission(this)
        Log.d(TAG, "onCreate: ")
        applicationScope.launch {
            testActivityNameList.forEach {
                when (it) {
                    TestActivity::class.java.canonicalName -> {
                        screenShotAllState(this@UiTestLauncherActivity)
                    }
                }
            }
        }
    }

}

/**
 * 获取外部转码目录
 */
internal fun getUiTestDirectory(): File {
    val file = File(getExternalStorageDirectory(), "netdiskUiTest")
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