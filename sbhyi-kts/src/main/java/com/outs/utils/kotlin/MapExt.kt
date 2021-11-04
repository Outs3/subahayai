package com.outs.utils.kotlin

/**
 * author: Outs3
 * e-mail: 3.3nosekai@gmail.com
 * date: 2021/4/20 9:48
 * desc:
 */
inline fun <K, V> MutableMap<K, V>.getOrPut(
    key: K,
    defaultValue: () -> V,
    onPut: (MutableMap<K, V>) -> Unit
): V {
    val value = get(key)
    return if (value == null) {
        val answer = defaultValue()
        put(key, answer)
        onPut(this)
        answer
    } else {
        value
    }
}
