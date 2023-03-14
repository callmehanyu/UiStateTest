package com.zhy.uistate

import android.graphics.Bitmap
import com.zhy.application.UiTestApplication
import com.zhy.bitmap.getImageFromAssetsFile

internal val bitmap: Bitmap? by lazy {
	"timg.jpeg".getImageFromAssetsFile(UiTestApplication.context)
}