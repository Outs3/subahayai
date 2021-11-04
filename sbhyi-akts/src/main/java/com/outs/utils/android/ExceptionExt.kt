package com.outs.utils.android

/**
 * author: Outs3
 * e-mail: 3.3nosekai@gmail.com
 * date: 2021/10/29 15:24
 * desc:
 */
fun tryOrToast(action: () -> Unit) = try {
    action()
} catch (e: Throwable) {
    e.message?.toast()
}