package com.outs.core.android.compose

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp

/**
 * author: Outs3
 * e-mail: 3.3nosekai@gmail.com
 * date: 2022/3/24 10:02
 * desc:
 */
@Composable
fun Int.textUnitDp(): TextUnit = with(LocalDensity.current) { this@textUnitDp.dp.toSp() }
