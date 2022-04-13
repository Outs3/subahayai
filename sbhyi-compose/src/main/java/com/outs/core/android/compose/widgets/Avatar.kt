package com.outs.core.android.compose.widgets

import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale

/**
 * author: Outs3
 * e-mail: 3.3nosekai@gmail.com
 * date: 2022/4/13 14:15
 * desc:
 */
@Composable
fun Avatar(
    modifier: Modifier = Modifier,
    model: Any?,
    contentDescription: String? = "头像",
    placeholder: Painter? = null,
    error: Painter? = placeholder,
    fallback: Painter? = error,
    alignment: Alignment = Alignment.Center,
    contentScale: ContentScale = ContentScale.Crop,
    alpha: Float = DefaultAlpha,
    colorFilter: ColorFilter? = null,
    filterQuality: FilterQuality = DrawScope.DefaultFilterQuality,
) = coil.compose.AsyncImage(
    model = model,
    contentDescription = contentDescription,
    modifier = modifier,
    placeholder = placeholder,
    error = error,
    fallback = fallback,
    alignment = alignment,
    contentScale = contentScale,
    alpha = alpha,
    colorFilter = colorFilter,
    filterQuality = filterQuality
)