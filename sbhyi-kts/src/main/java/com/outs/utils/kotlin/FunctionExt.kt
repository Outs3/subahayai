package com.outs.utils.kotlin

/**
 * author: Outs3
 * e-mail: 3.3nosekai@gmail.com
 * date: 2021/4/2 15:50
 * desc:
 */
fun emptyFunc() {}

fun <T> emptyFunc(t: T) {}

val emptyAction: () -> Unit = ::emptyFunc

val emptyConsumer: (Any?) -> Unit = ::emptyFunc

inline fun <T> T.doit(block: (T) -> Unit) {
    block(this)
}