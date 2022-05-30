package com.outs.utils.kotlin

import java.util.*

/**
 * author: Outs3
 * e-mail: 3.3nosekai@gmail.com
 * date: 2021/4/1 11:42
 * desc:
 */
fun String.emptyToNull(): String? = if (isEmpty()) null else this

fun String.blankToNull(): String? = if (isBlank()) null else this

fun String.emptyTo(s: String): String = if (isEmpty()) s else this

fun String.blankTo(s: String): String = if (isBlank()) s else this

fun String?.nullOrEmptyTo(s: String): String = if (isNullOrEmpty()) s else this

fun String?.nullOrBlankTo(s: String): String = if (isNullOrBlank()) s else this

fun String?.nullOrEmptyTo(onGet: () -> String): String = if (isNullOrEmpty()) onGet() else this

fun String?.nullOrBlankTo(onGet: () -> String): String = if (isNullOrBlank()) onGet() else this

fun String.safeSubString(range: IntRange): String {
    val start = if (range.first >= length) 0 else range.first
    val end = if (range.last >= length) length - 1 else range.last
    return substring(IntRange(start, end))
}

/**
 * @see Iterable.joinTo
 */
@Deprecated("Please use Iterable.joinToString instead.")
fun Iterable<String>.join(separator: String = ""): String {
    val iterator = this.iterator()
    val accumulator = StringBuilder()
    if (iterator.hasNext()) accumulator.append(iterator.next())
    while (iterator.hasNext()) {
        accumulator.append(separator).append(iterator.next())
    }
    return accumulator.toString()
}

fun String.groupByIndex(
    groupMemberCount: Int,
    outGroupSpace: (groupIndex: Int) -> String = { " " },
    outChar: (groupIndex: Int, groupCount: Int, memberIndex: Int, char: Char) -> String
): String {
    val ret = StringBuilder()
    val groupCount = length / groupMemberCount + (if (0 == length % groupMemberCount) 0 else 1)
    var groupIndex = 0
    for ((index, i) in (0 until length).withIndex()) {
        val memberIndex = index % groupMemberCount
        if (0 != index && 0 == memberIndex) {
            groupIndex++
            ret.append(outGroupSpace(groupIndex))
        }
        ret.append(outChar(groupIndex, groupCount, memberIndex, this[i]))
    }

    return ret.toString()
}

fun String.formatBankCardNum(): String =
    groupByIndex(4) { groupIndex, groupCount, _, char -> if (0 != groupIndex && groupCount - 1 != groupIndex) "*" else char.toString() }

@Suppress("NewApi")
fun ByteArray.encodeByBase64(): String = let(Base64.getEncoder()::encodeToString)

@Suppress("NewApi")
fun String.decodeByBase64(): ByteArray = let(Base64.getDecoder()::decode)
