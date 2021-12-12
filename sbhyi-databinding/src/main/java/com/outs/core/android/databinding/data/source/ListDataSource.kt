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

    //是否初始化（是否已经加载过数据）
    val isInit = MutableLiveData<Boolean>(false)

    //是否加载中
    val isLoading = MutableLiveData<Boolean>()

    //加载报错
    val error = MutableLiveData<Throwable>()

    //是否展示暂无数据框
    val showEmptyView by lazy {
        MediatorLiveData<Boolean>().apply {
            val onChange: (Boolean) -> Unit = {
                val isInit = isInit.value ?: false
                val isEmpty = isEmpty.value ?: true
                val isLoading = isLoading.value ?: false
                postOnNot(isInit && isEmpty && !isLoading)
            }
            addSource(isInit, onChange)
            addSource(isEmpty, onChange)
            addSource(isLoading, onChange)
        }
    }

    abstract suspend fun requestData(): MutableList<T>

    protected fun getList(onComplete: () -> Unit) {
        launchOnIO {
            isLoading.postOnNot(true)
            try {
                requestData().let(::loadList)
            } catch (e: Throwable) {
                if (e is LazyDataSourceException) {
                    //这里说明此DataSource有些懒加载的参数还没有加载结束 等参数加载后再调用refresh重新加载列表
                } else {
                    error.postValue(e)
                    e.e()
                }
            }
            isLoading.postOnNot(false)
            onComplete()
        }
    }

    override fun calcEmptyAndPosition() {
        super.calcEmptyAndPosition()
        isInit.postOnNot(true)
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