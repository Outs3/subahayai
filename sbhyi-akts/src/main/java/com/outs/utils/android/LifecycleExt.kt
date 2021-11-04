package com.outs.utils.android

import android.content.BroadcastReceiver
import android.content.Context
import android.content.IntentFilter
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.outs.utils.android.lifecycle.AutoCloseLifecycleObserver


/**
 * author: Outs3
 * e-mail: 3.3nosekai@gmail.com
 * date: 2021/4/9 15:17
 * desc:
 */
fun Lifecycle.regReceiver(
    context: Context,
    receiver: BroadcastReceiver,
    intentFilter: IntentFilter,
) {
    doOnDestroy {
        context.unregisterReceiver(receiver)
    }
    context.registerReceiver(receiver, intentFilter)
}

fun Lifecycle.doOnCreate(once: Boolean = true, block: () -> Unit) {
    addObserver(object : DefaultLifecycleObserver {
        override fun onCreate(owner: LifecycleOwner) {
            super.onCreate(owner)
            if (once) removeObserver(this)
            block()
        }
    }
    )
}

fun Lifecycle.doOnDestroy(block: () -> Unit) {
    addObserver(object : AutoCloseLifecycleObserver {
        override fun onDestroy(owner: LifecycleOwner) {
            super.onDestroy(owner)
            block()
        }
    }
    )
}
