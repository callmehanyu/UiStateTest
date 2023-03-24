package com.zhy.launcher

import android.app.ActivityGroup
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.base.applicationScope
import com.zhy.R
import com.zhy.demo.TestActivity
import com.zhy.launcher.screenshot.screenShotAllState
import kotlinx.coroutines.launch

private const val TAG = "UiTestLauncherActivity"
const val LAUNCHER_DEBUG_ACTIVITY_START_KEY_ACTIVITY_LIST = "activity_list"

internal class UiStateTestLauncherActivity : ActivityGroup() {

    private val testActivityNameList: List<String> by lazy {
        intent.getStringExtra(LAUNCHER_DEBUG_ACTIVITY_START_KEY_ACTIVITY_LIST)?.split(',')
            ?: emptyList()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group_ui_test_launcher)
        findViewById<TextView>(R.id.tv).setOnClickListener {
            applicationScope.launch {
                screenShotAllState(this@UiStateTestLauncherActivity)
            }
        }
        com.base.perm.checkPermission(this)
        Log.d(TAG, "onCreate: ")
        applicationScope.launch {
            testActivityNameList.forEach {
                when (it) {
                    TestActivity::class.java.canonicalName -> {
                        screenShotAllState(this@UiStateTestLauncherActivity)
                    }
                }
            }
        }
    }

}
