package com.outs.utils.kotlin

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * author: Outs3
 * e-mail: 3.3nosekai@gmail.com
 * date: 2022/4/8 17:40
 * desc:
 */
fun <T> MutableStateFlow<T>.update(scope: CoroutineScope, action: T.() -> T) {
    scope.launch { update(action) }
}

inline fun <T> MutableStateFlow<T>.update(function: T.() -> T) {
    while (true) {
        val prevValue = value
        val nextValue = function(prevValue)
        if (compareAndSet(prevValue, nextValue)) {
            return
        }
    }
}