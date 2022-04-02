package com.outs.core.android.compose

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp

/**
 * author: Outs3
 * e-mail: 3.3nosekai@gmail.com
 * date: 2022/4/2 13:42
 * desc:
 */
fun Modifier.drawIndicatorLine(lineWidth: Dp, color: Color): Modifier = drawBehind {
    val strokeWidth = lineWidth.value * density
    val y = size.height - strokeWidth / 2
    drawLine(
        color,
        Offset(0f, y),
        Offset(size.width, y),
        strokeWidth
    )
}