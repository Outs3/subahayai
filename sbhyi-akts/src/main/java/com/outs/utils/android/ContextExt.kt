package com.outs.utils.android

import android.content.Context
import android.content.pm.ApplicationInfo
import android.os.Build
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import com.outs.utils.kotlin.tryOrNull
import com.outs.utils.kotlin.typeOfOrNull

/**
 * author: Outs3
 * e-mail: 3.3nosekai@gmail.com
 * date: 2021/4/14 17:47
 * desc:
 */

val Context.inputMethodManager: InputMethodManager
    get() = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

//显示软键盘
fun Context.showKeyboard(et: EditText) {
    et.requestFocus()
    inputMethodManager.showSoftInput(et, InputMethodManager.SHOW_IMPLICIT)
}

fun Context.hideKeyboard(view: View) {
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

fun Context.getAppName(): Unit? = tryOrNull {
    packageManager.getApplicationInfo(packageName, 0).let { packageManager.getApplicationLabel(it) }
        .typeOfOrNull()
}

fun Context.getAppVersionName(): String? =
    tryOrNull { packageManager.getPackageInfo(packageName, 0).versionName }

fun Context.getAppVersionCode(): Long? =
    tryOrNull {
        packageManager.getPackageInfo(packageName, 0).let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                it.longVersionCode
            } else {
                it.versionCode.toLong()
            }
        }
    }

fun Context.isDebugApp(): Boolean =
    tryOrNull { 0 != applicationInfo.flags.and(ApplicationInfo.FLAG_DEBUGGABLE) } ?: false

