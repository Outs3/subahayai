package com.outs.core.android.compose.widgets

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Description
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.items
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshState
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.outs.core.android.compose.DefaultLoadMoreIndicator
import com.outs.core.android.compose.textUnitDp
import com.outs.core.android.compose.theme.Gray999
import com.outs.utils.kotlin.typeOf

/**
 * author: Outs3
 * e-mail: 3.3nosekai@gmail.com
 * date: 2022/4/8 17:39
 * desc:
 */
fun LazyPagingItems<*>.isError() = loadState.refresh is LoadState.Error
fun LazyPagingItems<*>.isEmpty() = 0 == itemCount
fun LazyPagingItems<*>.isLoadMore() = LoadState.Loading == loadState.append

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
    listContent: LazyListScope.(innerContent: LazyListScope.() -> Unit) -> Unit = { innerContent -> innerContent() },
    onRefresh: () -> Unit = { lazyPagingItems.refresh() },
    indicatorPadding: PaddingValues = PaddingValues(),
    fixIndicatorTitlePadding: Boolean = true,
    key: ((item: T) -> Any)? = null,
    itemContent: @Composable LazyItemScope.(value: T?) -> Unit
) {
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
        indicatorPadding = if (!fixIndicatorTitlePadding) indicatorPadding else PaddingValues(
            top = indicatorPadding.calculateTopPadding() + titleHeightDp,
            bottom = indicatorPadding.calculateBottomPadding(),
            start = indicatorPadding.calculateStartPadding(LayoutDirection.Ltr),
            end = indicatorPadding.calculateEndPadding(LayoutDirection.Ltr)
        ),
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
                lazyPagingItems.isEmpty() -> itemByTypeFillContent(
                    type = "Empty",
                    content = emptyContent
                )
                lazyPagingItems.isError() -> itemByTypeFillContent(
                    type = "Error",
                    content = { errorContent(lazyPagingItems.loadState.refresh.typeOf<LoadState.Error>().error) })
                else -> {
                    listContent {
                        innerContent()
                        if (lazyPagingItems.isLoadMore()) {
                            item(contentType = "LoadMore") {
                                loadMoreIndicator()
                            }
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

@Preview(widthDp = 375, heightDp = 675)
@Composable
private fun PreviewErrorLayout() {
    ListErrorLayout()
}

@Composable
fun ListErrorLayout(text: String = "暂无数据") {
    ListErrorLayout(columnContent = {
        Text(
            text = text,
            modifier = Modifier.padding(top = 12.dp),
            color = Gray999,
            fontSize = 14.textUnitDp(),
            textAlign = TextAlign.Center
        )
    })
}

@Composable
fun ListErrorLayout(
    boxContent: @Composable BoxScope.() -> Unit = {},
    columnContent: @Composable ColumnScope.() -> Unit = {
        Text(
            text = "暂无数据",
            modifier = Modifier.padding(top = 12.dp),
            color = Gray999,
            fontSize = 14.textUnitDp(),
            textAlign = TextAlign.Center
        )
    }
) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Outlined.Description,
                contentDescription = "未找到数据",
                modifier = Modifier.size(60.dp),
                tint = Gray999
            )
            columnContent()
        }
        boxContent()
    }
}