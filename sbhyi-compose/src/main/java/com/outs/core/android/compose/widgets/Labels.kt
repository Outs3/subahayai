package com.outs.core.android.compose.widgets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.outs.core.android.compose.theme.Gray333
import com.outs.core.android.compose.theme.Gray666

@Preview(widthDp = 375, heightDp = 675)
@Composable
private fun PreviewLabels() {
    Column(modifier = Modifier.padding(10.dp)) {
        DefaultLabel(title = "昵称：", value = "咖啡牛奶")
    }
}

@Composable
fun DefaultLabel(
    modifier: Modifier = Modifier,
    title: String,
    value: String?,
    valueClickable: Boolean = false,
    titleColor: Color = Gray666,
    titleFontSize: TextUnit = 16.sp,
    textColor: Color = if (valueClickable) MaterialTheme.colorScheme.primary else Gray333,
    textFontSize: TextUnit = titleFontSize,
    onValueClick: () -> Unit = {}
) {
    DefaultLabel(
        modifier = modifier,
        title = title,
        titleColor = titleColor,
        titleFontSize = titleFontSize,
    ) {
        val clickableModifier =
            if (valueClickable) Modifier.clickable(onClick = onValueClick) else Modifier
        Text(
            text = value ?: "",
            modifier = Modifier.then(clickableModifier),
            color = textColor,
            fontSize = textFontSize,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun DefaultLabel(
    modifier: Modifier = Modifier,
    title: String,
    titleColor: Color = Gray666,
    titleFontSize: TextUnit = 16.sp,
    content: @Composable RowScope.() -> Unit
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            color = titleColor,
            fontSize = titleFontSize
        )
        content()
    }
}
