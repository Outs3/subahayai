package com.outs.utils.android

import android.app.Notification
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.core.os.bundleOf
import com.blankj.utilcode.util.RomUtils
import com.outs.utils.kotlin.method
import com.outs.utils.kotlin.ref
import com.outs.utils.kotlin.tryOr

/**
 * author: Outs3
 * e-mail: 3.3nosekai@gmail.com
 * date: 2021/4/11 17:21
 * desc:
 */
fun Context.getLauncherClassName(): String? = getLauncherComponentName()?.className

fun Context.getLauncherComponentName(): ComponentName? =
    packageManager.getLaunchIntentForPackage(packageName)?.component

fun trySetBadgeCount(
    unreadTotal: Int,
    context: Context? = null,
    notification: Notification? = null
) {
    when {
        RomUtils.isXiaomi() -> {
            notification?.setBadgeCountForMi(unreadTotal)
        }
        RomUtils.isHuawei() -> {
            context?.setBadgeCountForHuawei(unreadTotal)
        }
        RomUtils.isVivo() -> {
            context?.setBadgeCountForVivo(unreadTotal)
        }
        RomUtils.isOppo() -> {
            context?.setBadgeCountForOppo(unreadTotal)
        }
        RomUtils.isSamsung() -> {
            context?.setBadgeCountForSamsung(unreadTotal)
        }
        else -> {
            notification?.setBadgeCountForMi(unreadTotal)
        }
    }
}

fun Notification.setBadgeCountForMi(count: Int) {
    tryOr(print = true) {
        ref<Any>("extraNotification")?.also { extraNotification ->
            extraNotification
                .method("setMessageCount", true, Int::class.java)
                ?.invoke(extraNotification, count)
        }
    }
}

const val URI_HUAWEI_BADGE_CHANGE = "content://com.huawei.android.launcher.settings/badge/"

fun Context.setBadgeCountForHuawei(total: Int) {
    tryOr(print = true) {
        val packageName = packageName
        val className =
            getLauncherClassName() ?: throw RuntimeException("Launcher class not found!")
        contentResolver.call(
            Uri.parse(URI_HUAWEI_BADGE_CHANGE), "change_badge", null, bundleOf(
                "package" to packageName,
                "class" to className,
                "badgenumber" to total
            )
        )
    }
}

const val ACTION_SAMSUNG_UPDATE_BADGE_COUNT = "android.intent.action.BADGE_COUNT_UPDATE"

fun Context.setBadgeCountForSamsung(total: Int) {
    tryOr(print = true) {
        val packageName = packageName
        val className =
            getLauncherClassName() ?: throw RuntimeException("Launcher class not found!")
        val intent = Intent(ACTION_SAMSUNG_UPDATE_BADGE_COUNT).putExtras(
            bundleOf(
                "badge_count" to total,
                "badge_count_package_name" to packageName,
                "badge_count_class_name" to className
            )
        )
        sendBroadcast(intent)
    }
}

fun Context.setBadgeCountForOppo(total: Int) {
    tryOr {
        val number = if (0 == total) -1 else total
        val intent = Intent("com.oppo.unsettledevent")
        intent.putExtra("pakeageName", packageName)
        intent.putExtra("number", number)
        intent.putExtra("upgradeNumber", number)
        if (packageManager.queryBroadcastReceivers(intent, 0).let { 0 < it.size }) {
            sendBroadcast(intent)
        } else {
            contentResolver.call(
                Uri.parse("content://com.android.badge/badge"),
                "setAppBadgeCount",
                null,
                Bundle().apply { putInt("app_badge_count", number) }
            )
        }
    }
}

fun Context.setBadgeCountForVivo(total: Int) {
    val intent = Intent("launcher.action.CHANGE_APPLICATION_NOTIFICATION_NUM")
    intent.putExtra("packageName", packageName)
    val className =
        getLauncherClassName() ?: throw RuntimeException("Launcher class not found!")
    intent.putExtra("className", className)
    intent.putExtra("notificationNum", total)
    sendBroadcast(intent)
}