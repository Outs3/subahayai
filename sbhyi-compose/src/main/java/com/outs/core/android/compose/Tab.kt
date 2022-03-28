package com.outs.core.android.compose

import android.graphics.drawable.Drawable
import androidx.fragment.app.Fragment

/**
 * author: Outs3
 * e-mail: 3.3nosekai@gmail.com
 * date: 2022/3/24 9:58
 * desc:
 */
data class Tab(
    val name: String,
    val route: String = name,
    val icon: Drawable? = null,
    val fragmentClass: Class<out Fragment>? = null
)