package com.outs.utils.android

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResult
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.blankj.utilcode.util.ActivityUtils
import com.outs.utils.android.launch.ILaunchOwner
import com.outs.utils.kotlin.typeOfOrNull

/**
 * author: Outs3
 * e-mail: 3.3nosekai@gmail.com
 * date: 2021/4/9 15:17
 * desc:
 */
val topActivity: Activity?
    get() = ActivityUtils.getTopActivity()?.typeOfOrNull<Activity>()

val topAppCompatActivity: AppCompatActivity?
    get() = ActivityUtils.getTopActivity()?.typeOfOrNull<AppCompatActivity>()

val topLaunchOwner: ILaunchOwner?
    get() = ActivityUtils.getTopActivity()?.typeOfOrNull()

inline fun <reified T> Context.newIntent(
    extras: Bundle? = null,
    withFlags: Int? = null,
    block: Intent.() -> Unit = {}
): Intent =
    Intent(this, T::class.java).apply {
        extras?.let(this::putExtras)
        withFlags?.let(this::addFlags)
        block()
    }

val ActivityResult.isSuccess get() = Activity.RESULT_OK == resultCode

val ActivityResult.isCancel get() = Activity.RESULT_CANCELED == resultCode

fun Activity.finishOk(intent: Intent?) {
    setResult(Activity.RESULT_OK, intent)
    finish()
}

fun Activity.finishOk(extra: Bundle? = null) {
    finishOk(extra?.let { Intent().putExtras(extra) })
}

fun Activity.finishOk(pair: Pair<String, Any>) {
    finishOk(bundleOf(pair))
}

fun Activity.finishResult(result: Int, extra: Bundle? = null) {
    setResult(result, extra?.let { Intent().putExtras(extra) })
    finish()
}

fun Activity.finishResult(result: Int, pair: Pair<String, Any>) {
    finishResult(result, bundleOf(pair))
}

fun ComponentActivity.regReceiver(receiver: BroadcastReceiver, intentFilter: IntentFilter) {
    lifecycle.regReceiver(this, receiver, intentFilter)
}

inline fun <reified T : Activity> Fragment.startActivity(vararg data: Pair<String, Any>) {
    activity?.startActivity<T>(*data)
}

inline fun <reified T : Activity> Fragment.startActivity(
    extras: Bundle? = null,
    withFlags: Int? = null
) {
    activity?.startActivity<T>(extras, withFlags)
}

inline fun <reified T : Activity> Context.startActivity(vararg data: Pair<String, Any>) =
    startActivity<T>(bundleOf(*data))

inline fun <reified T : Activity> Context.startActivity(
    extras: Bundle? = null,
    withFlags: Int? = null
) = startActivity(newIntent<T>(extras, withFlags))

fun Activity.hasPermission(permission: String) =
    PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(this, permission)