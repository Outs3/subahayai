package com.outs.utils.android

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner

/**
 * author: Outs3
 * e-mail: 3.3nosekai@gmail.com
 * date: 2021/5/6 16:55
 * desc:
 */
inline fun <reified VM : ViewModel> ViewModelStoreOwner.viewModel() =
    ViewModelProvider(this)[VM::class.java]

inline fun <reified VM : ViewModel> ViewModelStoreOwner.viewModel(vararg args: Any) =
    ViewModelProvider(
        this,
        ArgsFactory(args.map { it.javaClass }.toTypedArray(), *args)
    )[VM::class.java]

class ArgsFactory(
    private val classes: Array<Class<*>>,
    private vararg val args: Any
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        modelClass.getConstructor(*classes).newInstance(*args)
}