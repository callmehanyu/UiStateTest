package com.zhy.launcher

import android.app.ActivityGroup
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.os.Environment.getExternalStorageDirectory
import android.os.Handler
import android.util.Log
import android.widget.FrameLayout
import android.widget.RelativeLayout
import com.base.applicationScope
import com.base.bitmap.BitmapUtils
import com.zhy.R
import com.zhy.demo.TestActivity
import com.zhy.demo.mock.testUiStateCollection_List
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File

private const val TAG = "UiTestLauncherActivity"
const val LAUNCHER_DEBUG_ACTIVITY_START_KEY_ACTIVITY_LIST = "activity_list"

class UiTestLauncherActivity : ActivityGroup() {

    private val testActivityNameList: List<String> by lazy {
        intent.getStringExtra(LAUNCHER_DEBUG_ACTIVITY_START_KEY_ACTIVITY_LIST)?.split(',')
            ?: emptyList()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group_ui_test_launcher)
        com.base.perm.checkPermission(this)
        Log.d(TAG, "onCreate: ")
        val rootView = findViewById<FrameLayout>(R.id.rootView)
        applicationScope.launch {
            testActivityNameList.forEach {
                when (it) {
                    "com.zhy.demo.TestActivity" -> {
                        screenShot(rootView)
                    }
                }
            }
        }
    }

    private suspend fun screenShot(rootView: FrameLayout, ) {
        testUiStateCollection_List.forEachIndexed { index, testUiStateCollection ->
            rootView.removeAllViews()
            val view = localActivityManager.startActivity(
                TestActivity::class.java.canonicalName,
                Intent(this@UiTestLauncherActivity, TestActivity::class.java)
            ).decorView
            (localActivityManager.currentActivity as TestActivity).updateView(
                testUiStateCollection
            )

            rootView.addView(
                view, 0, RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.MATCH_PARENT
                )
            )

            rootView.post {
                val bitmap = BitmapUtils.getBitmapFromView(rootView) ?: return@post
                val dir = getUiTestDirectory()
                BitmapUtils.compressToFile(
                    bitmap,
                    dir.absolutePath + "/com_zhy_demo_TestActivity_${index}.jpg"
                )
            }
            delay(1000)
        }
    }

}

/**
 * 获取外部转码目录
 */
private fun getUiTestDirectory(): File {
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