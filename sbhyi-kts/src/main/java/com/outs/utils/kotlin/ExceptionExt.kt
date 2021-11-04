package com.outs.utils.kotlin

/**
 * author: Outs3
 * e-mail: 3.3nosekai@gmail.com
 * date: 2021/4/2 15:54
 * desc:
 */
fun String.throwRuntime(): Unit = throw RuntimeException(this)

fun tryWithoutThrow(action: () -> Unit) {
    try {
        action()
    } catch (e: Exception) {
    }
}

fun <T> tryOr(print: Boolean = false, action: () -> T): T? = try {
    action()
} catch (e: Exception) {
    if (print) {
        e.d()
    }
    null
}

fun <T> tryOr(action: () -> T, or: T): T = try {
    action()
} catch (e: Exception) {
    or
}

fun <T> tryOrNull(action: () -> T): T? = try {
    action()
} catch (e: Exception) {
    null
}

fun <T> tryOr(action: () -> T, or: (Exception) -> T) = try {
    action()
} catch (e: Exception) {
    or(e)
}

suspend fun <T> suspendTryOrNull(action: suspend () -> T): T? = try {
    action()
} catch (e: Exception) {
    null
}
