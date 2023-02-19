package com.zhy.launcher.biz

import android.content.Intent
import android.widget.FrameLayout
import android.widget.RelativeLayout
import com.base.bitmap.BitmapUtils
import com.zhy.R
import com.zhy.demo.mock.testUiState_List
import com.zhy.launcher.UiTestLauncherActivity
import com.zhy.launcher.getUiTestDirectory
import com.zhy.unittest.TestUiState
import kotlin.Int
import kotlin.Unit
import kotlinx.coroutines.delay

internal suspend fun screenShotAllState(launcher: UiTestLauncherActivity): Unit {
  testUiState_List.forEachIndexed { index, uiState ->
      	screenShot(launcher, uiState, index)
      }
}

internal suspend fun screenShot(
  launcher: UiTestLauncherActivity,
  uiState: TestUiState,
  index: Int
): Unit {
  val rootView = launcher.findViewById<FrameLayout>(R.id.root_view)
      rootView.removeAllViews()
      val view = launcher.localActivityManager.startActivity(
          com.zhy.demo.TestActivity::class.java.canonicalName,
          Intent(launcher, com.zhy.demo.TestActivity::class.java)
      ).decorView
      (launcher.localActivityManager.currentActivity as
          com.zhy.demo.TestActivity).updateView(uiState)

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
              dir.absolutePath + "/com_zhy_demo_TestActivity_screenShot_$index.jpg"
          )
      }
      delay(1000)
}
