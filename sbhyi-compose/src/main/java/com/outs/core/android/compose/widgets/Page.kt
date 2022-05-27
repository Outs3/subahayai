package com.outs.core.android.compose.widgets

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.outs.core.android.compose.theme.GrayF7

/**
 * author: Outs3
 * e-mail: 3.3nosekai@gmail.com
 * date: 2022/4/15 9:30
 * desc:
 */
@Composable
fun backAction(): () -> Unit = LocalOnBackPressedDispatcherOwner
    .current
    ?.onBackPressedDispatcher
    ?.let { it::onBackPressed }
    ?: {}

@Composable
fun BackPage(
    modifier: Modifier = Modifier,
    title: String,
    scrollable: Boolean = true,
    contentLeft: @Composable RowScope.() -> Unit = {},
    contentRight: @Composable RowScope.() -> Unit = {},
    contentParentModifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    Page(
        modifier = modifier,
        scrollable = scrollable,
        titleBar = {
            TitleBar(
                title = title,
                contentLeft = {
                    BackIcon(modifier = Modifier.clickable(onClick = backAction()))
                    contentLeft()
                },
                contentRight = {
                    contentRight()
                }
            )
        },
        contentParentModifier = contentParentModifier,
        content = content
    )
}

@Composable
fun Page(
    modifier: Modifier = Modifier,
    scrollable: Boolean = true,
    titleBar: @Composable ColumnScope.() -> Unit,
    contentParentModifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    Column(modifier = modifier.fillMaxSize()) {
        titleBar()
        Box(
            modifier = contentParentModifier
                .fillMaxSize()
                .background(color = GrayF7)
                .then(if (scrollable) Modifier.verticalScroll(rememberScrollState()) else Modifier),
            content = content
        )
    }
}