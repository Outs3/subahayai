package com.outs.core.android.compose

import androidx.compose.foundation.lazy.LazyListState
import com.google.accompanist.swiperefresh.SwipeRefreshState
import com.outs.utils.android.IntPagingSource
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * author: Outs3
 * e-mail: 3.3nosekai@gmail.com
 * date: 2022/4/8 17:41
 * desc:
 */
abstract class LoadingIntPagingSource<T : Any> : IntPagingSource<T>() {

    protected val isLoading by lazy { MutableStateFlow(false) }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, T> {
        onLoadStart(params)
        val ret = super.load(params)
        onLoadResult(ret)
        return ret
    }

    open suspend fun onLoadStart(params: LoadParams<Int>) {
        isLoading.value = true
    }

    open suspend fun onLoadResult(result: LoadResult<Int, T>) {
        isLoading.value = false
    }

}

abstract class RefreshIntPagingSource<T : Any>(
    protected val refresh: SwipeRefreshState,
    protected val listState: LazyListState? = null,
    protected val onRefreshFinish: suspend () -> Unit = {},
) : LoadingIntPagingSource<T>() {

    override suspend fun onLoadStart(params: LoadParams<Int>) {
        super.onLoadStart(params)
        if (null == params.key || 1 == params.key) {
            refresh.isRefreshing = true
        }
    }

    override suspend fun onLoadResult(result: LoadResult<Int, T>) {
        super.onLoadResult(result)
        if (refresh.isRefreshing) {
            refreshFinish()
        }
    }

    private suspend fun refreshFinish() {
        refresh.isRefreshing = false
        listState?.scrollToItem(0)
        onRefreshFinish()
    }

}