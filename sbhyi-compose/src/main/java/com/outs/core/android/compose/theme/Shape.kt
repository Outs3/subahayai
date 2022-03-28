package com.outs.core.android.compose.theme

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection

/**
 * author: Outs3
 * e-mail: 3.3nosekai@gmail.com
 * date: 2022/3/28 18:27
 * desc:
 */
fun bottomRectShape(width: Dp) = object : Shape {
    override fun createOutline(size: Size, layoutDirection: LayoutDirection, density: Density) =
        Outline.Rectangle(
            Rect(
                0f,
                size.height - with(density) { width.toPx() },
                size.width,
                size.height
            )
        )

    override fun toString(): String = "BottomRectShape"
}
