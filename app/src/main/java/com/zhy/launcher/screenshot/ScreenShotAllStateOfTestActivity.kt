package com.zhy.launcher.screenshot

import android.content.Intent
import android.widget.FrameLayout
import android.widget.RelativeLayout
import com.zhy.R
import com.zhy.`file`.getUiTestDirectory
import com.zhy.bitmap.compressToFile
import com.zhy.bitmap.getBitmap
import com.zhy.launcher.UiTestLauncherActivity
import com.zhy.uistate.testUiState_List
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
          val bitmap = rootView.getBitmap() ?: return@post
          val dir = getUiTestDirectory()
          bitmap.compressToFile(
              dir.absolutePath + "/com_zhy_demo_TestActivity_screenShot_$index.jpg"
          )
      }
      delay(1000)
}
