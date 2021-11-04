package com.outs.utils.kotlin

import kotlin.math.max
import kotlin.math.min

/**
 * author: Outs3
 * e-mail: 3.3nosekai@gmail.com
 * date: 2021/4/2 15:21
 * desc:
 */
fun <T> List<T>.getOrNull(position: Int) =
    if (position >= this.size || 0 > position) null else this[position]

fun <T> List<T>.remGet(position: Int) = this[position.rem(size)]

fun <T> List<T>.safeSlice(range: kotlin.ranges.IntRange): List<T> =
    slice(max(0, range.first)..min(size - 1, range.last))

inline fun <reified T, reified R> Iterable<T>.reduce(seed: R, operation: (R, T) -> R): R {
    val iterator = this.iterator()
    var accumulator: R = seed
    while (iterator.hasNext()) {
        accumulator = operation(accumulator, iterator.next())
    }
    return accumulator
}

fun <T, M : MutableMap<String, MutableList<T>>> Iterable<T>.groupToMap(
    ret: M,
    getGroup: (T, Int) -> String
): M {
    val iterator = iterator()
    var index = 0
    while (iterator.hasNext()) {
        val item = iterator.next()
        ret.getOrPut(getGroup(item, index)) { ArrayList<T>() }
        index++
    }
    return ret
}

fun <T> Iterable<T>.groupByIndex(groupMemberCount: Int): ArrayList<ArrayList<T>> {
    val ret = ArrayList<ArrayList<T>>()
    val iterator = iterator()
    var index = 0
    var currentGroup: ArrayList<T>? = null
    var currentGroupCount = 0
    while (iterator.hasNext()) {
        if (groupMemberCount == currentGroupCount) {
            currentGroupCount = 0
            currentGroup = ArrayList()
            ret.add(currentGroup)
        }
        val item = iterator.next()
        currentGroup?.add(item)
        index++
    }
    return ret
}

fun <T> MutableList<T>.reset(data: Collection<T>) {
    clear()
    addAll(data)
}

fun <T> MutableSet<T>.toggle(item: T) = if (contains(item)) remove(item) else add(item)

fun <T> List<T>.centerOrNull(): T? = tryOrNull {
    when (size) {
        0 -> null
        1 -> get(0)
        else -> get((size - 1) / 2)
    }
}