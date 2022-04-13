package com.outs.core.android.compose

import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember

/**
 * author: Outs3
 * e-mail: 3.3nosekai@gmail.com
 * date: 2022/4/13 14:12
 * desc:
 */
@Deprecated(
    "please use androidx.activity.compose.BackHandler",
    ReplaceWith("androidx.activity.compose.BackHandler")
)
@Composable
fun OnBack(key: Any? = Unit, onBack: () -> Unit) {
    val onBackPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    val callback = remember {
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                onBack()
            }
        }
    }
    DisposableEffect(key1 = key, effect = {
        onBackPressedDispatcher?.addCallback(callback)
        onDispose {
            callback.remove()
        }
    })
}

@Composable
fun defaultBackAction(): () -> Unit =
    LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher?.let { it::onBackPressed }
        ?: {}