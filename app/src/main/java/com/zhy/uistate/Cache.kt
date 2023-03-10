package com.zhy.uistate

import android.graphics.Bitmap
import com.zhy.bitmap.getBitmap

internal val bitmap: Bitmap? = java.io.File(android.os.Environment.getExternalStorageDirectory(),
	"/timg.jpeg").getBitmap(true, 50,50)