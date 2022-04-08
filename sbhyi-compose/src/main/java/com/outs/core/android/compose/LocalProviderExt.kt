package com.outs.core.android.compose

import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

/**
 * author: Outs3
 * e-mail: 3.3nosekai@gmail.com
 * date: 2022/4/8 17:42
 * desc:
 */
fun noLocalProvidedFor(name: String): Nothing {
    error("CompositionLocal $name not present")
}

val LocalFragmentManager =
    staticCompositionLocalOf<FragmentManager> { noLocalProvidedFor("LocalFragmentManager") }

@Composable
fun AppCompatActivity.ProvideCompositions(
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(
        LocalFragmentManager.provides(supportFragmentManager),
        content = content
    )
}

@Composable
fun Fragment.ProvideCompositions(
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(
        LocalFragmentManager.provides(childFragmentManager),
        content = content
    )
}
