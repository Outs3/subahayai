package com.outs.core.android.compose

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.Pager
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

/**
 * author: Outs3
 * e-mail: 3.3nosekai@gmail.com
 * date: 2022/3/18 15:43
 * desc:
 */

@Composable
fun DefaultLoadMoreIndicator() {
    Box(Modifier.fillMaxWidth()) {
        Text(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(10.dp),
            text = "加载中..."
        )
    }
}

@Composable
fun <T : Any> SwipeRefreshList(
    modifier: Modifier = Modifier,
    lazyPagingItems: LazyPagingItems<T>,
    key: ((item: T) -> Any)? = null,
    swipeRefresh: @Composable (lazyPagingItems: LazyPagingItems<T>, isRefreshing: Boolean, content: @Composable () -> Unit) -> Unit = { lazyPagingItems, isRefreshing, content ->
        SwipeRefresh(
            state = rememberSwipeRefreshState(isRefreshing = isRefreshing),
            onRefresh = lazyPagingItems::refresh,
            modifier = modifier,
            content = content
        )
    },
    lazyColumn: @Composable (content: LazyListScope.() -> Unit) -> Unit = { content ->
        LazyColumn(content = content)
    },
    loadMoreIndicator: @Composable () -> Unit = { DefaultLoadMoreIndicator() },
    noMoreIndicator: @Composable () -> Unit = {},
    item: @Composable LazyItemScope.(item: T?) -> Unit,
) {
    val isRefreshing = LoadState.Loading == lazyPagingItems.loadState.refresh
    val isLoadMore =
        LoadState.Loading == lazyPagingItems.loadState.append || LoadState.Loading == lazyPagingItems.loadState.prepend
    swipeRefresh(lazyPagingItems, isRefreshing) {
        lazyColumn {
            items(items = lazyPagingItems, key = key) { item -> item(item) }
            if (isLoadMore) {
                item {
                    loadMoreIndicator()
                }
            } else {
                item {
                    noMoreIndicator()
                }
            }
        }
    }
}

@Composable
fun <T : Any> SwipeRefreshList(
    modifier: Modifier = Modifier,
    pager: Pager<Int, T>,
    key: ((item: T) -> Any)? = null,
    swipeRefresh: @Composable (lazyPagingItems: LazyPagingItems<T>, isRefreshing: Boolean, content: @Composable () -> Unit) -> Unit = { lazyPagingItems, isRefreshing, content ->
        SwipeRefresh(
            state = rememberSwipeRefreshState(isRefreshing = isRefreshing),
            onRefresh = lazyPagingItems::refresh,
            modifier = modifier,
            content = content
        )
    },
    lazyColumn: @Composable (content: LazyListScope.() -> Unit) -> Unit = { content ->
        LazyColumn(content = content)
    },
    loadMoreIndicator: @Composable () -> Unit = { DefaultLoadMoreIndicator() },
    noMoreIndicator: @Composable () -> Unit = {},
    item: @Composable LazyItemScope.(item: T?) -> Unit,
) {
    SwipeRefreshList(
        modifier,
        pager.flow.collectAsLazyPagingItems(),
        key,
        swipeRefresh,
        lazyColumn,
        loadMoreIndicator,
        noMoreIndicator,
        item
    )
}
