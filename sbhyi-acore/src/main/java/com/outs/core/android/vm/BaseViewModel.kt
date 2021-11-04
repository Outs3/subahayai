package com.outs.core.android.vm

import android.view.View
import androidx.lifecycle.*
import com.outs.core.android.config.ProgressStatus
import com.outs.utils.kotlin.d
import com.outs.utils.kotlin.e
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicInteger

/**
 * author: Outs3
 * e-mail: 3.3nosekai@gmail.com
 * date: 2021/4/27 11:00
 * desc:
 */
abstract class BaseViewModel : ViewModel(), DefaultLifecycleObserver {

    val isEmpty = MutableLiveData<Boolean>()
    var onClick: View.OnClickListener? = null
    val progressCount by lazy { AtomicInteger(0) }
    val progressStatus by lazy { MutableLiveData<ProgressStatus>() }

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        observe(owner)
    }

    open fun observe(owner: LifecycleOwner) {

    }

    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
    }

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
    }

    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
    }

    override fun onStop(owner: LifecycleOwner) {
        super.onStop(owner)
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        onClick = null
    }

    fun showProgress() {
        val count = progressCount.incrementAndGet()
        if (0 < count) progressStatus.postValue(ProgressStatus.SHOW_PROGRESS)
    }

    fun cancelProgress() {
        val count = progressCount.decrementAndGet()
        if (0 >= count) progressStatus.postValue(ProgressStatus.CANCEL_PROGRESS)
    }

    fun launchOnUI(
        withProgress: Boolean = false,
        ignoreCancel: Boolean = true,
        onError: (Throwable) -> Unit = this::launchErrorHandle,
        onFinally: () -> Unit = {},
        block: suspend CoroutineScope.() -> Unit
    ): Job = viewModelScope.launch {
        if (withProgress)
            showProgress()
        try {
            block()
        } catch (e: Throwable) {
            if (!ignoreCancel || e !is CancellationException) {
                onError(e)
            } else {
                "Job is cancel!".d()
            }
        } finally {
            onFinally()
            if (withProgress)
                cancelProgress()
        }
    }

    open fun launchErrorHandle(e: Throwable) {
        e.e()
    }

}