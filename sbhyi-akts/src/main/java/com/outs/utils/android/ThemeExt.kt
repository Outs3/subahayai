package com.outs.utils.android

import android.content.Context
import android.graphics.Color
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt

/**
 * author: Outs3
 * e-mail: 3.3nosekai@gmail.com
 * date: 2021/5/15 16:25
 * desc:
 */
@ColorInt
fun Context.attrColor(@AttrRes attr: Int): Int =
    theme
        ?.obtainStyledAttributes(intArrayOf(attr))
        ?.let {
            try {
                it.getColor(0, Color.TRANSPARENT)
            } finally {
                it.recycle()
            }
        } ?: Color.TRANSPARENT