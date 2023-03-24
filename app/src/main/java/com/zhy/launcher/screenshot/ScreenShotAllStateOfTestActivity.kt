package com.zhy.launcher.screenshot

import android.content.Intent
import android.widget.FrameLayout
import android.widget.RelativeLayout
import com.google.gson.Gson
import com.zhy.R
import com.zhy.`file`.getUiTestDirectory
import com.zhy.bitmap.compressToFile
import com.zhy.bitmap.getBitmap
import com.zhy.file.ZipUtils
import com.zhy.file.mkdirIfExist
import com.zhy.file.saveJsonToFile
import com.zhy.launcher.UiStateTestLauncherActivity
import com.zhy.uistate.app.*
import com.zhy.uistate.app.testUiState_List
import com.zhy.unittest.TestUiState
import kotlin.Int
import kotlin.Unit
import kotlinx.coroutines.delay
import org.json.JSONObject
import java.io.File

/**
 * com.zhy.unittest.TestUiState_List
 */
public val testUiState_List1: List<TestUiState> = listOf(
    testUiState_0, testUiState_1, testUiState_2,
    testUiState_3, )

internal suspend fun screenShotAllState(launcher: UiStateTestLauncherActivity): Unit {
    val dir = getUiTestDirectory()
    val bizDir = File(dir, testUiState_List.first().javaClass.canonicalName ?: "").apply {
		mkdirIfExist()
	}
	val uiStateScreenShotDir = File(bizDir, "screenShot/").apply {
		mkdirIfExist()
	}
	val uiStateDir = File(bizDir, "uiState/").apply {
		mkdirIfExist()
	}
	testUiState_List1.forEachIndexed { index, uiState ->
		screenShot(launcher, uiState, index, uiStateScreenShotDir)
		uiState.saveJsonToFile(index, uiStateDir)
	}
	ZipUtils.zipFiles(listOf(bizDir), File(dir, "${bizDir.name}.zip"))
	bizDir.deleteRecursively()
}

private suspend fun screenShot(
	launcher: UiStateTestLauncherActivity,
	uiState: TestUiState,
	index: Int,
	uiStateDir: File,
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
		bitmap.compressToFile("${uiStateDir.absolutePath}/$index.jpg")
	}
	delay(1000)
}
