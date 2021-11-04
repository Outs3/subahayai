package com.outs.core.android.databinding.debug

import com.outs.core.android.databinding.data.source.newListDataSource
import com.outs.core.android.logs
import com.outs.utils.kotlin.finish
import com.outs.utils.kotlin.launchOnIO
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

/**
 * author: Outs3
 * e-mail: 3.3nosekai@gmail.com
 * date: 2021/10/8 13:34
 * desc:
 */
class LogHelper(
    private val delayMillis: Long = 50,
    autoStart: Boolean = true
) {

    val logData by lazy { newListDataSource { logs.data } }

    private var updateFlag: Boolean = false

    private val onDataChangedListener: () -> Unit = ::onDataChanged

    private var job: Job? = null

    init {
        if (autoStart) start()
    }

    fun start() {
        logs.addListener(onDataChangedListener)
        job?.finish()
        job = launchOnIO {
            while (true) {
                if (updateFlag) {
                    updateFlag = false
                    suspendCoroutine<Unit> { continuation ->
                        logData.refresh { continuation.resume(Unit) }
                    }
                }
                delay(delayMillis)
            }
        }
        logData.refresh()
    }

    fun stop() {
        logs.removeListener(onDataChangedListener)
        job?.finish()
    }

    private fun onDataChanged() {
        updateFlag = true
    }

}