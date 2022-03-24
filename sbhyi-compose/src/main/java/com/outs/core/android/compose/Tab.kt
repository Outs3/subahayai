package com.outs.core.android.compose

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.FragmentManager

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

@Composable
fun FragmentOf(
    modifier: Modifier = Modifier,
    fm: FragmentManager,
    fragmentClass: Class<out Fragment>,
    args: Bundle? = null
) {
    AndroidView(
        factory = { c ->
            FragmentContainerView(c).apply {
                val containerViewId = View.generateViewId()
                id = containerViewId
                val tag = fragmentClass.simpleName
                val fragment = fm.findFragmentByTag(tag)
                if (null == fragment) {
                    fm.beginTransaction()
                        .add(containerViewId, fragmentClass, args, fragmentClass.simpleName)
                        .commit()
                } else {
                    fm.beginTransaction()
                        .remove(fragment)
                        .commit()
                    fm.executePendingTransactions()
                    fm.beginTransaction()
                        .add(containerViewId, fragment, tag)
                        .commit()
                }
            }
        },
        modifier = modifier
    )
}
