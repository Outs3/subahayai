package com.outs.core.android.databinding.data.adapter

import androidx.databinding.ViewDataBinding
import com.outs.core.android.databinding.holder.AutoItem
import com.outs.core.android.databinding.holder.DataBindingViewHolder
import com.outs.core.android.databinding.holder.OnModelClickListener

/**
 * author: Outs3
 * e-mail: 3.3nosekai@gmail.com
 * date: 2021/12/21 9:50
 * desc:
 */
typealias ICreateHolder<T> = (viewType: Int) -> DataBindingViewHolder<T, out ViewDataBinding>

object CreateHolderFactory {
    fun <T : Any> auto(
        itemLayoutId: Int,
        onModelClick: OnModelClickListener<T>? = null
    ): ICreateHolder<T> = { AutoItem(itemLayoutId, onModelClick) }
}
