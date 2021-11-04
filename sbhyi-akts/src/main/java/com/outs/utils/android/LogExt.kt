package com.outs.utils.android

import com.outs.utils.kotlin.d
import com.outs.utils.kotlin.e

/**
 * author: Outs3
 * e-mail: 3.3nosekai@gmail.com
 * date: 2021/10/29 15:07
 * desc:
 */
fun String.d(
    withToast: Boolean = false,
    tag: String? = null
) {
    d(tag)
    if (withToast) toastOnDebug()
}

fun String.e(
    withToast: Boolean = true,
    tag: String? = null
) {
    e(tag)
    if (withToast) longToast()
}

fun Throwable.e(withToast: Boolean = true) {
    e()
    if (withToast) {
        (if (isDebug) toString() else message)?.longToast()
    }
}

fun String.dToast() = d(withToast = true)

