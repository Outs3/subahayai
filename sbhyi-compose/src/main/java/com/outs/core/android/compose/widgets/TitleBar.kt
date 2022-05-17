package com.outs.core.android.compose.widgets

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material3.Icon
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.outs.core.android.compose.theme.Gray333
import com.outs.core.android.compose.theme.Gray666

/**
 * author: Outs3
 * e-mail: 3.3nosekai@gmail.com
 * date: 2022/3/28 17:30
 * desc:
 */
@Composable
fun TitleBar(
    title: String,
    fontSize: TextUnit = 17.sp,
    contentLeft: @Composable RowScope.() -> Unit = {},
    contentRight: @Composable RowScope.() -> Unit = {},
) {
    SmallTopAppBar(
        title = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .defaultMinSize(minHeight = 42.dp)
            ) {
                Row(
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(horizontal = 10.dp),
                    content = contentLeft
                )
                Text(
                    text = title,
                    modifier = Modifier.align(Alignment.Center),
                    color = Gray333,
                    fontSize = fontSize,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )
                Row(
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(horizontal = 10.dp),
                    content = contentRight
                )
            }
        },
        colors = TopAppBarDefaults.smallTopAppBarColors(
            containerColor = Color.White
        )
    )
}

@Composable
fun BackIcon(modifier: Modifier = Modifier, tint: Color = Gray666) {
    Icon(
        imageVector = Icons.Filled.ArrowBackIos,
        contentDescription = "返回",
        modifier = modifier
            .padding(10.dp)
            .size(20.dp),
        tint = tint
    )
}

@Composable
fun MoreIcon(modifier: Modifier = Modifier, tint: Color = Gray666) {
    Icon(
        imageVector = Icons.Filled.MoreHoriz,
        contentDescription = "更多",
        modifier = modifier
            .padding(10.dp)
            .size(20.dp),
        tint = tint
    )
}

@Composable
fun TitleBar(
    title: String,
    withBack: Boolean = true,
    withMore: Boolean = false,
    onBack: () -> Unit = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher?.let { it::onBackPressed }
        ?: {},
    onMore: () -> Unit = {}
) {
    TitleBar(
        title = title,
        contentLeft = {
            if (withBack) {
                BackIcon(modifier = Modifier.clickable(onClick = onBack))
            }
        },
        contentRight = {
            if (withMore) {
                MoreIcon(Modifier.clickable(onClick = onMore))
            }
        }
    )
}