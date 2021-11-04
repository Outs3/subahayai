package com.outs.utils.kotlin

import kotlinx.coroutines.Job
import java.util.concurrent.ScheduledFuture

/**
 * author: Outs3
 * e-mail: 3.3nosekai@gmail.com
 * date: 2021/4/12 17:34
 * desc:
 */
fun ScheduledFuture<*>.finish(mayInterruptIfRunning: Boolean = true) {
    if (!isCancelled || !isDone)
        cancel(mayInterruptIfRunning)
}

fun Job.finish() {
    if (!isCancelled || !isCompleted)
        cancel()
}