package com.outs.core.android.compose

import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner

/**
 * author: Outs3
 * e-mail: 3.3nosekai@gmail.com
 * date: 2022/4/13 14:11
 * desc:
 */
@Composable
fun OnLifecycleEvent(onEvent: (owner: LifecycleOwner, event: Lifecycle.Event) -> Unit) {
    val eventHandler = rememberUpdatedState(newValue = onEvent)
    val lifecycleOwner = rememberUpdatedState(newValue = LocalLifecycleOwner.current)

    DisposableEffect(key1 = lifecycleOwner.value, effect = {
        val lifecycle = lifecycleOwner.value.lifecycle
        val observer = LifecycleEventObserver { owner, event ->
            eventHandler.value(owner, event)
        }
        lifecycle.addObserver(observer)
        onDispose {
            lifecycle.removeObserver(observer)
        }
    })
}

@Composable
fun OnLifecycleResume(action: () -> Unit) {
    OnLifecycleEvent(onEvent = { owner, event ->
        if (event == Lifecycle.Event.ON_RESUME) {
            action()
        }
    })
}

@Composable
fun isOnFront(): MutableState<Boolean> {
    val isOnFront = remember { mutableStateOf(false) }
    OnLifecycleEvent(onEvent = { owner, event ->
        when (event) {
            Lifecycle.Event.ON_RESUME -> isOnFront.value = true
            else -> isOnFront.value = false
        }
    })
    return isOnFront
}