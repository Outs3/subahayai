package com.outs.core.android

import com.outs.core.android.databinding.data.source.DataSource
import com.outs.utils.kotlin.ILogger
import com.outs.utils.kotlin.addLogger


/**
 * author: Outs3
 * e-mail: 3.3nosekai@gmail.com
 * date: 2021/10/29 15:44
 * desc:
 */
val logs: DataSource<String> by lazy {
    addLogger(object : ILogger {

        override fun d(tag: String?, msg: String) {
            msg.addToLog()
        }

        override fun e(tag: String?, msg: String) {
            msg.addToLog()
        }

    })
    DataSource.empty()
}

val netLogs by lazy { DataSource.empty<String>() }

val lifecycleLogs by lazy { DataSource.empty<String>() }

fun String.addToLog() = logs.addOnIO(this)
