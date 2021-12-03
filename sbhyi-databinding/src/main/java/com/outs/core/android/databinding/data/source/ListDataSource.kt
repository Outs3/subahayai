package com.outs.core.android.databinding.data.source

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.outs.utils.android.e
import com.outs.utils.android.postOnNot
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

    //是否展示暂无数据框
    val showEmptyView by lazy {
        MediatorLiveData<Boolean>().apply {
            val onChange: (Boolean) -> Unit = {
                val isEmpty = isEmpty.value ?: true
                val isLoading = isLoading.value ?: false
                postOnNot(isEmpty && !isLoading)
            }
            addSource(isEmpty, onChange)
            addSource(isLoading, onChange)
        }
    }

    abstract suspend fun requestData(): MutableList<T>

    protected fun getList(onComplete: () -> Unit) {
        launchOnIO {
            isEmpty.postValue(false)
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