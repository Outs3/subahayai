package com.outs.utils.android

import android.util.SparseArray

/**
 * author: Outs3
 * e-mail: 3.3nosekai@gmail.com
 * date: 2021/12/14 11:40
 * desc:
 */
inline fun <T> SparseArray<T>.getOrPut(
    key: Int,
    defaultValue: () -> T,
    onPut: (SparseArray<T>) -> Unit = {}
): T = get(key)
    ?: defaultValue()
        .also { value ->
            put(key, value)
            onPut(this)
        }