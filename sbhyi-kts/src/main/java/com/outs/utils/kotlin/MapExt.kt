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
    onPut: (MutableMap<K, V>) -> Unit = {}
): V = get(key)
    ?: defaultValue()
        .also { value ->
            put(key, value)
            onPut(this)
        }


