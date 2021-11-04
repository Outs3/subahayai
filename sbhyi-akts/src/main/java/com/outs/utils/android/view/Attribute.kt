package com.outs.utils.android.view

import android.content.res.TypedArray
import androidx.lifecycle.MutableLiveData

/**
 * author: Outs3
 * e-mail: 3.3nosekai@gmail.com
 * date: 2021/5/15 16:25
 * desc:
 */
abstract class Attribute<T> {
    //@StyleableRes
    protected abstract val styleableId: Int

    protected abstract val defValue: T?

    val data by lazy { MutableLiveData<T?>(defValue) }

    protected abstract fun getFromAttrs(typedArray: TypedArray): T?

    protected abstract fun applyData()

}