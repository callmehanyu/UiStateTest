package com.zhy.application

import android.app.Application
import android.content.Context

class UiTestApplication : Application() {

	var context: Context? = null
		get() = field!!

	override fun onCreate() {
		super.onCreate()
		context = applicationContext
	}
}