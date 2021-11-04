package com.outs.utils.android

import android.graphics.Color
import android.view.Gravity
import androidx.annotation.DrawableRes
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.outs.utils.kotlin.emptyToNull

/**
 * author: Outs3
 * e-mail: 3.3nosekai@gmail.com
 * date: 2021/5/15 16:25
 * desc:
 */
fun String.toast() {
    emptyToNull()?.let(ToastUtils::showShort)
}

fun String.longToast() {
    emptyToNull()?.let(ToastUtils::showLong)
}

fun String.toastOnDebug() {
    if (isDebug) emptyToNull()?.let(ToastUtils::showShort)
}

fun String.toastOnCenter(gravity: Int = Gravity.CENTER) {
    LogUtils.d(this)
    ToastUtils.make()
        .setMode(ToastUtils.MODE.DARK)
        .setGravity(gravity, 0, 0)
        .show(this)
}

fun String.toastOnCenterWithTopIcon(
    gravity: Int = Gravity.CENTER,
    @DrawableRes topIcon: Int = android.R.drawable.ic_dialog_info
) {
    LogUtils.d(this)
    ToastUtils.make()
        .setMode(ToastUtils.MODE.DARK)
        .setBgColor(0x4DFFFFFF)
        .setTextColor(Color.WHITE)
        .setTextSize(14)
        .setTopIcon(topIcon)
        .setGravity(gravity, 0, 0)
        .show(this)
}
