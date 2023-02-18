package com.zhy.launcher.biz

import android.content.Intent
import android.widget.RelativeLayout
import com.base.bitmap.BitmapUtils
import com.zhy.demo.TestActivity
import com.zhy.demo.mock.testUiStateCollection_List
import com.zhy.launcher.UiTestLauncherActivity
import com.zhy.launcher.getUiTestDirectory
import com.zhy.unittest.TestUiStateCollection
import kotlinx.android.synthetic.main.activity_group_ui_test_launcher.*
import kotlinx.coroutines.delay

internal suspend fun UiTestLauncherActivity.screenShotAllStateOfTestActivity() {
    testUiStateCollection_List.forEachIndexed { index, testUiStateCollection ->
        screenShot(testUiStateCollection, index)
    }
}

private suspend fun UiTestLauncherActivity.screenShot(testUiStateCollection: TestUiStateCollection, index: Int) {
    root_view.removeAllViews()
    val view = localActivityManager.startActivity(
        TestActivity::class.java.canonicalName,
        Intent(this, TestActivity::class.java)
    ).decorView
    (localActivityManager.currentActivity as TestActivity).updateView(testUiStateCollection)

    root_view.addView(
        view, 0, RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT,
            RelativeLayout.LayoutParams.MATCH_PARENT
        )
    )

    root_view.post {
        val bitmap = BitmapUtils.getBitmapFromView(root_view) ?: return@post
        val dir = getUiTestDirectory()
        BitmapUtils.compressToFile(
            bitmap,
            dir.absolutePath + "/com_zhy_demo_TestActivity_screenShot_${index}.jpg"
        )
    }
    delay(1000)
}