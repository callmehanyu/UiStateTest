package com.base.screenshot.listener

import android.graphics.Bitmap

/**
 * 截屏监听
 */
interface ScreenshotListener {

    suspend fun onScreenSuc(bitmap:Bitmap)

}