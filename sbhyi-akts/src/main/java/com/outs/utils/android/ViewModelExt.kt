package com.outs.utils.android

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.ViewModelInitializer

/**
 * author: Outs3
 * e-mail: 3.3nosekai@gmail.com
 * date: 2021/5/6 16:55
 * desc:
 */
inline fun <reified VM : ViewModel> viewModel() =
    viewModel(VM::class.java)

inline fun <reified VM : ViewModel> viewModel(vararg args: Any) =
    viewModel(VM::class.java, *args)

fun <VM : ViewModel> viewModel(viewModelClass: Class<VM>, vararg args: Any) =
    createViewModel(viewModelClass, args.map { it.javaClass }.toTypedArray(), *args)
        .create(viewModelClass)

fun <VM : ViewModel> viewModel(
    viewModelClass: Class<VM>,
    classes: Array<Class<*>>,
    args: Array<Any?>
) = createViewModel(viewModelClass, classes, args)
    .create(viewModelClass)

fun <VM : ViewModel> createViewModel(
    viewModelClass: Class<VM>,
    classes: Array<Class<*>>,
    vararg args: Any
) = ViewModelProvider.Factory.from(
    ViewModelInitializer(
        viewModelClass
    ) {
        viewModelClass.getConstructor(*classes).newInstance(*args)
    }
)
