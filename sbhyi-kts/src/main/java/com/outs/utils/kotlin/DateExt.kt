package com.outs.utils.kotlin

import java.text.SimpleDateFormat
import java.util.*

/**
 * author: Outs3
 * e-mail: 3.3nosekai@gmail.com
 * date: 2020/12/5 9:42
 * desc:
 */
fun Long.toText(): String {
    val second: Long = 1000
    val minute: Long = second * 60
    val hour: Long = minute * 60
    val day = hour * 24
    val current = System.currentTimeMillis()
    val diff = current - this   //时间差
    return when {
        diff <= minute -> "刚刚"
        diff <= hour -> "${diff / minute}分钟前"
        diff <= day -> "${diff / hour}小时前"
        else -> "${diff / day}天前"
    }
}

/**
 * 秒单位转化为时间格式 00:00:00
 */
fun Long.hourFormat(hideHourOnZero: Boolean = false): String {
    val h: Long = this / 3600
    val m: Long = this % 3600 / 60
    val s: Long = this % 3600 % 60

    val hs = if (h < 10) "0$h" else "$h"
    val ms = if (m < 10) "0$m" else "$m"
    val ss = if (s < 10) "0$s" else "$s"

    return if (hideHourOnZero && 0L == h) "$ms:$ss" else "$hs:$ms:$ss"
}

/**
 * 秒单位转化为时间格式 00:00
 */
fun Long.minuteFormat(): String {
    val m: Long = this / 60
    val s: Long = this % 60

    val ms = if (m < 10) "0$m" else "$m"
    val ss = if (s < 10) "0$s" else "$s"

    return "$ms:$ss"
}

fun Int.minuteFormat(): String {
    val m: Int = this / 60
    val s: Int = this % 60

    val ms = if (m < 10) "0$m" else "$m"
    val ss = if (s < 10) "0$s" else "$s"

    return "$ms:$ss"
}

fun Long.date(): String = this.format("yyyy-MM-dd")

fun Long.datetime(): String = this.format("yyyy-MM-dd HH:mm:ss")

fun Long.datetimeWithoutSecond(): String = this.format("yyyy-MM-dd HH:mm")

fun Long.datetimeWithMillis(): String = this.format("yyyy-MM-dd HH:mm:ss SSS")

fun String.format(time: Long) = SimpleDateFormat(this).format(time)

fun Long.format(pattern: String) = SimpleDateFormat(pattern).format(Date(this))

fun String.parseDate(pattern: String): Long = SimpleDateFormat(pattern).parse(this).time

/**
 * 获取今天是周几
 *      周一 0 ~ 周日 6
 */
fun getWeekOfDay(): Int = when (Calendar.getInstance(Locale.CHINA).get(Calendar.DAY_OF_WEEK)) {
    Calendar.SUNDAY -> 6
    Calendar.MONDAY -> 0
    Calendar.TUESDAY -> 1
    Calendar.WEDNESDAY -> 2
    Calendar.THURSDAY -> 3
    Calendar.FRIDAY -> 4
    Calendar.SATURDAY -> 5
    else -> 0
}

val dateFormat by lazy { SimpleDateFormat("yyyy-MM-dd_HH:mm:ss SSS") }

fun Date.simpleFormat(): String = dateFormat.format(this)