package com.outs.core.android.databinding.data.source

import androidx.lifecycle.MutableLiveData
import com.outs.utils.android.e
import com.outs.utils.kotlin.emptyAction
import com.outs.utils.kotlin.launchOnIO
import com.outs.utils.kotlin.reset
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

/**
 * author: Outs3
 * e-mail: 3.3nosekai@gmail.com
 * date: 2021/4/9 17:05
 * desc:
 */
abstract class ListDataSource<T> : DataSource<T>() {

    val isLoading = MutableLiveData<Boolean>()

    val error = MutableLiveData<Throwable>()

    abstract suspend fun requestData(): MutableList<T>

    protected fun getList(onComplete: () -> Unit) {
        launchOnIO {
            isLoading.postValue(true)
            try {
                requestData().let(::loadList)
            } catch (e: Throwable) {
                error.postValue(e)
                e.e()
            }
            isLoading.postValue(false)
            onComplete()
        }
    }

    open fun loadList(data: MutableList<T>) {
        this.data.reset(data)
        notifyAdapters()
    }

    open fun refresh(onComplete: () -> Unit = emptyAction) {
        getList(onComplete)
    }

    open suspend fun refreshAndWait() = suspendCoroutine<Unit> { continuation ->
        refresh { continuation.resume(Unit) }
    }

}