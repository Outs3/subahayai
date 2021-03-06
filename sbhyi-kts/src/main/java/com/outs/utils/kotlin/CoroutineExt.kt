package com.outs.utils.kotlin

import kotlinx.coroutines.*
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

/**
 * author: Outs3
 * e-mail: 3.3nosekai@gmail.com
 * date: 2021/4/8 15:25
 * desc:
 */
inline fun <T, R> T.suspendLet(block: (T) -> R): R = block(this)

fun <T> Continuation<T>.resumeOrException(ret: T?) {
    ret?.also { resume(it) } ?: resumeWithException(RuntimeException("Error: resume obj is empty!"))
}

fun launchOn(
    scope: CoroutineScope,
    onError: suspend CoroutineScope.(Throwable) -> Unit = { e -> e.e() },
    onFinally: () -> Unit = {},
    block: suspend CoroutineScope.() -> Unit
): Job = scope.launch {
    try {
        block()
    } catch (e: Throwable) {
        if (e !is CancellationException) {
            onError(e)
        } else {
            "Job is cancel!".d()
        }
    } finally {
        onFinally()
    }
}

fun launchOnMain(
    delay: Long = 0,
    onError: suspend CoroutineScope.(Throwable) -> Unit = { e -> e.e() },
    onFinally: () -> Unit = {},
    block: suspend CoroutineScope.() -> Unit
): Job = CoroutineScope(Dispatchers.Main).launch {
    try {
        if (0 < delay) delay(delay)
        block()
    } catch (e: Throwable) {
        if (e !is CancellationException) {
            onError(e)
        } else {
            "Job is cancel!".d()
        }
    } finally {
        onFinally()
    }
}

fun launchOnIO(
    onError: suspend CoroutineScope.(Throwable) -> Unit = { e -> e.e() },
    onFinally: () -> Unit = {},
    block: suspend CoroutineScope.() -> Unit
): Job = CoroutineScope(Dispatchers.IO).launch {
    try {
        block()
    } catch (e: Throwable) {
        if (e !is CancellationException) {
            onError(e)
        } else {
            "Job is cancel!".d()
        }
    } finally {
        onFinally()
    }
}