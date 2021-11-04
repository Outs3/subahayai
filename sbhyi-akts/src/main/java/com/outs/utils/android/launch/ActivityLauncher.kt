package com.outs.utils.android.launch

import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContract
import androidx.appcompat.app.AppCompatActivity
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

/**
 * author: Outs3
 * e-mail: 3.3nosekai@gmail.com
 * date: 2021/10/26 9:47
 * desc:
 */
open class ActivityLauncher<I, O>(
    activity: AppCompatActivity,
    contract: ActivityResultContract<I, O>
) {

    private var callback: ActivityResultCallback<O> = ActivityResultCallback {}

    private val launcher =
        activity.registerForActivityResult(contract) { callback.onActivityResult(it) }

    fun launch(input: I, callback: ActivityResultCallback<O>) {
        this.callback = callback
        launcher.launch(input)
    }

    private suspend fun <I, O> suspendLaunch(
        input: I,
        func: (I, ActivityResultCallback<O>) -> Unit
    ): O =
        suspendCoroutine { continuation -> func(input) { result -> continuation.resume(result) } }

    suspend fun launch(input: I): O = suspendLaunch(input, ::launch)

}
