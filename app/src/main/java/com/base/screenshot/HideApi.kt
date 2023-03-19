package com.base.screenshot

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Rect
import android.os.Build
import android.util.DisplayMetrics
import android.util.Log
import android.view.Surface
import java.io.DataOutputStream
import java.io.IOException
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method

//屏幕宽度1920，高度720
fun startScreenShot() {
	try {
		@SuppressLint("PrivateApi") val mClassType = Class.forName("android.view.SurfaceControl")
		val nativeScreenshotMethod: Method
		nativeScreenshotMethod = mClassType.getDeclaredMethod(
			"screenshot",
			Rect::class.java,
			Int::class.javaPrimitiveType,
			Int::class.javaPrimitiveType,
			Int::class.javaPrimitiveType
		)
		nativeScreenshotMethod.setAccessible(true)
		val sBitmap: Bitmap = nativeScreenshotMethod.invoke(
			mClassType,
			Rect(),
			1920,
			720,
			Surface.ROTATION_0
		) as Bitmap
		Log.d("MainActivity", "--> nativeScreenshotMethod after sBitmap=" + (sBitmap != null))
	} catch (e: ClassNotFoundException) {
		e.printStackTrace()
	} catch (e: NoSuchMethodException) {
		e.printStackTrace()
	} catch (e: IllegalAccessException) {
		e.printStackTrace()
	} catch (e: InvocationTargetException) {
		e.printStackTrace()
	}
}


fun Context.screenshot(): Bitmap? {
	val resources: Resources = this.getResources()
	val dm: DisplayMetrics = resources.getDisplayMetrics()
	var surfaceClassName = ""
	surfaceClassName = if (Build.VERSION.SDK_INT <= 17) {
		"android.view.Surface"
	} else {
		"android.view.SurfaceControl"
	}
	try {
		val c = Class.forName(surfaceClassName)
		val method = c.getMethod(
			"screenshot", *arrayOf<Class<*>?>(
				Int::class.javaPrimitiveType,
				Int::class.javaPrimitiveType
			)
		)
		method.isAccessible = true
		return method.invoke(null, dm.widthPixels, dm.heightPixels) as Bitmap
	} catch (e: IllegalAccessException) {
		e.printStackTrace()
	} catch (e: NoSuchMethodException) {
		e.printStackTrace()
	} catch (e: InvocationTargetException) {
		e.printStackTrace()
	} catch (e: ClassNotFoundException) {
		e.printStackTrace()
	}
	return null
}

fun screenShotByShell(filePath: String) {
	val shotCmd = "screencap -p $filePath \n"
	try {
		Runtime.getRuntime().exec(shotCmd)
	} catch (e: Exception) {
		e.printStackTrace()
	}
}


/**
 * 执行命令但不关注结果输出
 */
fun execRootCmdSilent(cmd: String): Int {
	var result = -1
	var dos: DataOutputStream? = null
	try {
		val p = Runtime.getRuntime().exec("su")
		dos = DataOutputStream(p.outputStream)
		Log.i("TAG", cmd)
		dos.writeBytes(
			"""
				$cmd
				
				""".trimIndent()
		)
		dos.flush()
		dos.writeBytes("exit\n")
		dos.flush()
		p.waitFor()
		result = p.exitValue()
	} catch (e: java.lang.Exception) {
		e.printStackTrace()
	} finally {
		if (dos != null) {
			try {
				dos.close()
			} catch (e: IOException) {
				e.printStackTrace()
			}
		}
	}
	return result
}