package com.outs.utils.kotlin

import java.math.RoundingMode
import java.text.DecimalFormat
import kotlin.math.max
import kotlin.math.min

/**
 * author: Outs3
 * e-mail: 3.3nosekai@gmail.com
 * date: 2021/4/1 12:00
 * desc:
 */
@Deprecated(
    "replace with kotlin.text.toIntOrNull",
    replaceWith = ReplaceWith("toIntOrNull()", imports = ["kotlin.text.toIntOrNull"])
)
fun String.safeInt(def: Int = 0): Int = try {
    toInt()
} catch (e: Throwable) {
    def
}

@Deprecated(
    "replace with kotlin.text.toLongOrNull",
    replaceWith = ReplaceWith("toLongOrNull()", imports = ["kotlin.text.toLongOrNull"])
)
fun String.safeLong(def: Long = 0L): Long = try {
    toLong()
} catch (e: Throwable) {
    def
}

@Deprecated(
    "replace with kotlin.text.toFloatOrNull",
    replaceWith = ReplaceWith("toFloatOrNull()", imports = ["kotlin.text.toFloatOrNull"])
)
fun String.safeFloat(def: Float = 0f): Float = try {
    toFloat()
} catch (e: Throwable) {
    def
}

@Deprecated(
    "replace with kotlin.text.toDoubleOrNull",
    replaceWith = ReplaceWith("toDoubleOrNull()", imports = ["kotlin.text.toDoubleOrNull"])
)
fun String.safeDouble(def: Double = 0.0): Double = try {
    toDouble()
} catch (e: Throwable) {
    def
}

val FILE_LENGTH = arrayOf("B", "KB", "MB", "GB", "TB")

fun Double.formatOne(): String = DecimalFormat("#.#").format(this)

/**
 * 保留小数点后两位
 */
fun Double.format(): String = DecimalFormat("#.##").format(this)

fun Float.format(): String = toDouble().format()

fun Float.format(newScale: Int): String =
    this.toBigDecimal().setScale(newScale, RoundingMode.UNNECESSARY).toString()

fun Long.divWithRem(other: Long): Long {
    val rem = if (0L == rem(other)) 0 else 1
    return div(other) + rem
}

fun Int.divWithRem(other: Int): Int {
    val rem = if (0 == rem(other)) 0 else 1
    return div(other) + rem
}

fun Int.formatFileLength(): String {
    var length = toFloat()
    var unit: String = FILE_LENGTH[0]
    var i = 1
    while (i < FILE_LENGTH.size && length >= 1024) {
        length /= 1024f
        unit = FILE_LENGTH[i]
        i++
    }
    return String.format("%.2f%s", length, unit)
}

fun Int.rangeOf(range: IntRange): Int = min(max(range.first, this), range.last)

fun Int.chsNum(): String = when {
    this > 10000 -> this.toDouble().div(10000).formatOne().let { "${it}万" }
    else -> this.toString()
}
