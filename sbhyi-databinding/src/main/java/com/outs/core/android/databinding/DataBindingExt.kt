package com.outs.core.android.databinding

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.outs.utils.android.appInstance
import com.outs.utils.kotlin.method

/**
 * author: Outs3
 * e-mail: 3.3nosekai@gmail.com
 * date: 2021/8/20 13:11
 * desc:
 */
// this: LayoutId
fun <T : ViewDataBinding> Int.inflateDataBinding(
    context: Context = appInstance,
    parent: ViewGroup? = null,
    attachToParent: Boolean = false
) = DataBindingUtil.inflate<T>(
    LayoutInflater.from(context),
    this,
    parent,
    attachToParent
)

inline fun <reified VDB : ViewDataBinding> inflateVDB(
    context: Context = appInstance,
    parent: ViewGroup? = null,
    attachToParent: Boolean = false
): VDB = VDB::class.java.method(
    "inflate",
    false,
    LayoutInflater::class.java,
    ViewGroup::class.java,
    Boolean::class.java
)?.invoke(null, LayoutInflater.from(context), parent, attachToParent) as VDB
