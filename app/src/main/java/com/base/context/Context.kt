package com.base.context

import android.content.Context
import android.content.Intent
import android.os.Parcelable
import androidx.fragment.app.FragmentActivity

private const val START_INTENT_PARAM: String = "start_intent_param"

/**
 * 获取打开Activity的Intent方法
 */
inline fun <reified T : FragmentActivity> Context.getStartIntent(param: Parcelable? = null): Intent {
    return Intent(this, T::class.java).apply {
        if (param == null) return@apply
        putStartParam(param)
    }
}

/**
 * put startParam进Intent
 */
fun Intent.putStartParam(param: Parcelable) {
    putExtra(START_INTENT_PARAM, param)
}

/**
 * 从Intent获取StartParam的方法
 */
fun <P : Parcelable> Intent.getStartParam(): P? {
    return kotlin.runCatching {
        getParcelableExtra<P>(START_INTENT_PARAM)
    }.onFailure {

    }.getOrNull()
}

