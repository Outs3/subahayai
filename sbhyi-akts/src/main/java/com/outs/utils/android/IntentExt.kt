package com.outs.utils.android

import android.app.Activity
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment

/**
 * author: Outs3
 * e-mail: 3.3nosekai@gmail.com
 * date: 2021/4/27 15:37
 * desc:
 */
fun Activity.injectIntent() = inject(intent?.extras)

inline fun <reified F : Fragment> F.withArgs(vararg pairs: Pair<String, Any>) = this.also {
    it.arguments = bundleOf(*pairs)
}

inline fun <reified F : Fragment> F.withArgs(arguments: Bundle) = this.also {
    it.arguments = arguments
}

fun Fragment.injectArgs() = inject(arguments)
