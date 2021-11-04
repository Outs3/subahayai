package com.outs.utils.kotlin

/**
 * author: Outs3
 * e-mail: 3.3nosekai@gmail.com
 * date: 2021/4/17 9:05
 * desc:
 */
fun <T> or(vararg os: T?) = os.firstOrNull { null != it }

fun or(vararg ss: String?) = ss.firstOrNull { !it.isNullOrEmpty() }

fun <T> or(vararg blocks: () -> T?): T? {
    for (block in blocks) {
        val ret = block()
        if (null != ret)
            return ret
    }
    return null
}

fun or(vararg blocks: () -> String): String {
    for (block in blocks) {
        val ret = block()
        if (ret.isNotEmpty())
            return ret
    }
    return ""
}

fun <T> Array<T>.getOrNull(position: Int) =
    if (position >= this.size || 0 > position) null else this[position]

fun <T> Array<T>.remGet(position: Int) = this[position.rem(size)]

fun ByteArray.toHexString(): String =
    joinToString { it.toInt().minus(Byte.MIN_VALUE).let(Integer::toHexString) }

fun ShortArray.toHexString(): String =
    joinToString { it.toInt().minus(Short.MIN_VALUE).let(Integer::toHexString) }

fun DoubleArray.format(): String =
    joinToString { it.format() }