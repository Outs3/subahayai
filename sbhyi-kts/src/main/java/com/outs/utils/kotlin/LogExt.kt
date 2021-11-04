package com.outs.utils.kotlin

import java.util.*
import kotlin.system.measureTimeMillis

/**
 * author: Outs3
 * e-mail: 3.3nosekai@gmail.com
 * date: 2021/4/13 15:29
 * desc:
 */
interface ILogger {
    fun d(tag: String? = null, msg: String)
    fun e(tag: String? = null, msg: String)
}

private var defaultLogger: ILogger = object : ILogger {
    override fun d(tag: String?, msg: String) {
        println("[${System.currentTimeMillis().datetime()}][${tag ?: "Log"}]$msg")
    }

    override fun e(tag: String?, msg: String) {
        System.err.println("[${System.currentTimeMillis().datetime()}][${tag ?: "Log"}]$msg")
    }
}

private var loggers: ArrayList<ILogger> = arrayListOf(defaultLogger)

private var logger = object : ILogger {
    override fun d(tag: String?, msg: String) {
        loggers.forEach { it.d(tag = tag, msg = msg) }
    }

    override fun e(tag: String?, msg: String) {
        loggers.forEach { it.e(tag = tag, msg = msg) }
    }

}

fun setLogger(new: ILogger) {
    loggers[0] = new
}

fun addLogger(new: ILogger) = loggers.add(new)

fun Any?.toLogString(): String = when (this) {
    is Date -> simpleFormat()
    is Throwable -> message ?: ""
    is ByteArray -> toList().toLogString()
    is IntArray -> toList().toLogString()
    is LongArray -> toList().toLogString()
    is List<*> -> joinToString(",", "[", "]", transform = Any?::toLogString)
    is String -> toString()
    else -> toString()
}

fun Any?.d(tag: String? = null) {
    logger.d(tag = tag, msg = toLogString())
}

fun Any?.e(tag: String? = null) {
    logger.e(tag = tag, msg = toLogString())
}

fun printBlockSpend(tag: String = "LogByOu", blockName: String = "Spend", block: () -> Unit) {
    measureTimeMillis(block).d(tag = "$tag-$blockName")
}

fun <R> printBlockSpend(tag: String = "LogByOu", blockName: String = "Spend", block: () -> R): R {
    val start = System.currentTimeMillis()
    val ret = block()
    val spend = System.currentTimeMillis() - start
    spend.d(tag = "$tag-$blockName")
    return ret
}