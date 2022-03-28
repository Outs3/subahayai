package com.outs.core.android.compose

import android.os.Bundle
import android.view.View
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.FragmentManager
import com.outs.core.android.compose.widgets.TitleBar

/**
 * author: Outs3
 * e-mail: 3.3nosekai@gmail.com
 * date: 2022/3/28 18:25
 * desc:
 */
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

@Composable
fun TitleAndFragment(
    modifier: Modifier = Modifier,
    title: String,
    titleLeft: @Composable RowScope.() -> Unit = {},
    titleRight: @Composable RowScope.() -> Unit = {},
    fm: FragmentManager,
    fragmentClass: Class<out Fragment>,
    args: Bundle? = null
) {
    Column(modifier = modifier) {
        TitleBar(title, contentLeft = titleLeft, contentRight = titleRight)
        FragmentOf(
            modifier = Modifier.fillMaxSize(),
            fm = fm,
            fragmentClass = fragmentClass,
            args = args
        )
    }
}