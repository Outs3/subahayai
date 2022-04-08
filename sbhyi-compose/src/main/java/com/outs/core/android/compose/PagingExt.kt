package com.outs.core.android.compose

import androidx.compose.foundation.lazy.LazyListState
import com.google.accompanist.swiperefresh.SwipeRefreshState
import com.outs.utils.android.IntPagingSource

/**
 * author: Outs3
 * e-mail: 3.3nosekai@gmail.com
 * date: 2022/4/8 17:41
 * desc:
 */
abstract class RefreshIntPagingSource<T : Any>(
    protected val refreshState: SwipeRefreshState? = null,
    protected val listState: LazyListState? = null
) : IntPagingSource<T>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, T> {
        refreshState?.isRefreshing = true
        val ret = super.load(params)
        refreshState?.isRefreshing = false
        listState?.scrollToItem(0)
        return ret
    }
}