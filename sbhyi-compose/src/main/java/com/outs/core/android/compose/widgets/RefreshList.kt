package com.outs.core.android.compose.widgets

import androidx.compose.foundation.lazy.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.items
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshState
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.outs.core.android.compose.DefaultLoadMoreIndicator
import com.outs.utils.kotlin.typeOf

/**
 * author: Outs3
 * e-mail: 3.3nosekai@gmail.com
 * date: 2022/4/8 17:39
 * desc:
 */
@Composable
fun <T : Any> RefreshList(
    modifier: Modifier = Modifier,
    swipeRefreshState: SwipeRefreshState = rememberSwipeRefreshState(isRefreshing = false),
    lazyListState: LazyListState = rememberLazyListState(),
    lazyPagingItems: LazyPagingItems<T>,
    isInit: Boolean = false,
    initContent: @Composable () -> Unit = {},
    errorContent: @Composable (Throwable) -> Unit = {},
    emptyContent: @Composable () -> Unit = {},
    loadMoreIndicator: LazyListScope.() -> Unit = { item { DefaultLoadMoreIndicator() } },
    onRefresh: () -> Unit = { lazyPagingItems.refresh() },
    key: ((item: T) -> Any)? = null,
    itemContent: @Composable LazyItemScope.(value: T?) -> Unit
) {
    lazyPagingItems.loadState
    val isEmpty = 0 == lazyPagingItems.itemCount
    val isError = lazyPagingItems.loadState.refresh is LoadState.Error
    val isLoadMore = LoadState.Loading == lazyPagingItems.loadState.append
    RefreshList(
        modifier = modifier,
        swipeRefreshState = swipeRefreshState,
        lazyListState = lazyListState,
        lazyPagingItems = lazyPagingItems,
        onRefresh = onRefresh,
        decorationBox = { innerContent ->
            when {
                isInit -> initContent()
                isEmpty -> emptyContent()
                isError -> errorContent(lazyPagingItems.loadState.refresh.typeOf<LoadState.Error>().error)
                else -> innerContent()
            }
        },
        decorationInnerBox = {
            if (isLoadMore) {
                loadMoreIndicator()
            }
        },
        key = key,
        itemContent = itemContent
    )
}

@Composable
fun <T : Any> RefreshList(
    modifier: Modifier = Modifier,
    swipeRefreshState: SwipeRefreshState = rememberSwipeRefreshState(isRefreshing = false),
    lazyListState: LazyListState = rememberLazyListState(),
    lazyPagingItems: LazyPagingItems<T>,
    onRefresh: () -> Unit = { lazyPagingItems.refresh() },
    decorationBox: @Composable (innerContent: @Composable () -> Unit) -> Unit = { innerContent -> innerContent() },
    decorationInnerBox: LazyListScope.() -> Unit = {},
    key: ((item: T) -> Any)? = null,
    itemContent: @Composable LazyItemScope.(value: T?) -> Unit,
) {
    SwipeRefresh(
        state = swipeRefreshState,
        onRefresh = onRefresh,
        modifier = modifier
    ) {
        decorationBox {
            LazyColumn(state = lazyListState) {
                items(
                    items = lazyPagingItems,
                    key = key,
                    itemContent = itemContent
                )
                decorationInnerBox()
            }
        }
    }
}
