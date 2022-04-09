package com.outs.core.android.compose.widgets

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
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
    titleContent: @Composable () -> Unit = {},
    initContent: @Composable () -> Unit = {},
    errorContent: @Composable (Throwable) -> Unit = {},
    emptyContent: @Composable () -> Unit = {},
    loadMoreIndicator: @Composable () -> Unit = { DefaultLoadMoreIndicator() },
    onRefresh: () -> Unit = { lazyPagingItems.refresh() },
    key: ((item: T) -> Any)? = null,
    itemContent: @Composable LazyItemScope.(value: T?) -> Unit
) {
    lazyPagingItems.loadState
    val isEmpty = 0 == lazyPagingItems.itemCount
    val isError = lazyPagingItems.loadState.refresh is LoadState.Error
    val isLoadMore = LoadState.Loading == lazyPagingItems.loadState.append
    val density = LocalDensity.current
    var totalHeight by remember { mutableStateOf(0) }
    var titleHeight by remember { mutableStateOf(0) }
    val titleHeightDp = with(density) { titleHeight.toDp() }
    val contentHeightDp = with(density) { (totalHeight - titleHeight).toDp() }
    RefreshList(
        modifier = modifier.onSizeChanged { size -> totalHeight = size.height },
        swipeRefreshState = swipeRefreshState,
        lazyListState = lazyListState,
        lazyPagingItems = lazyPagingItems,
        onRefresh = onRefresh,
        indicatorPadding = PaddingValues(top = titleHeightDp),
        decorationInnerBox = { innerContent ->
            item(contentType = "Title") {
                Box(modifier = Modifier.onSizeChanged { size -> titleHeight = size.height }) {
                    titleContent()
                }
            }
            fun itemByTypeFillContent(type: Any? = null, content: @Composable () -> Unit) {
                item(key = type, contentType = type) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(contentHeightDp)
                    ) {
                        content()
                    }
                }
            }
            when {
                isInit -> itemByTypeFillContent(type = "Init", content = initContent)
                isEmpty -> itemByTypeFillContent(type = "Empty", content = emptyContent)
                isError -> itemByTypeFillContent(
                    type = "Error",
                    content = { errorContent(lazyPagingItems.loadState.refresh.typeOf<LoadState.Error>().error) })
                else -> {
                    innerContent()
                    if (isLoadMore) {
                        item(contentType = "LoadMore") {
                            loadMoreIndicator()
                        }
                    }
                }
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
    indicatorAlignment: Alignment = Alignment.TopCenter,
    indicatorPadding: PaddingValues = PaddingValues(0.dp),
    decorationBox: @Composable (innerContent: @Composable () -> Unit) -> Unit = { innerContent -> innerContent() },
    decorationInnerBox: LazyListScope.(innerContent: LazyListScope.() -> Unit) -> Unit = { innerContent -> innerContent() },
    key: ((item: T) -> Any)? = null,
    itemContent: @Composable LazyItemScope.(value: T?) -> Unit,
) {
    SwipeRefresh(
        state = swipeRefreshState,
        onRefresh = onRefresh,
        modifier = modifier,
        indicatorAlignment = indicatorAlignment,
        indicatorPadding = indicatorPadding,
    ) {
        decorationBox {
            LazyColumn(state = lazyListState) {
                decorationInnerBox {
                    items(
                        items = lazyPagingItems,
                        key = key,
                        itemContent = itemContent
                    )
                }
            }
        }
    }
}
