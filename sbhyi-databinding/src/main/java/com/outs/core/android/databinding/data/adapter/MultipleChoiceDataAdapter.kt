package com.outs.core.android.databinding.data.adapter

import android.content.Context
import androidx.databinding.ViewDataBinding
import com.outs.core.android.databinding.holder.CheckableViewHolder
import com.outs.core.android.databinding.holder.CheckedAutoItem
import com.outs.core.android.databinding.holder.DataBindingViewHolder
import com.outs.core.android.databinding.holder.ICheckedParent
import com.outs.utils.kotlin.toggle

/**
 * author: Outs3
 * e-mail: 3.3nosekai@gmail.com
 * date: 2021/5/7 10:24
 * desc:
 */
open class MultipleChoiceDataAdapter<T : Any>(
    context: Context,
    data: com.outs.core.android.databinding.data.source.DataSource<T> = com.outs.core.android.databinding.data.source.DataSource.empty(),
    var onCheckChanged: ((T?, Int, Boolean) -> Unit)? = null,
    val checked: HashSet<T> = HashSet(),
    createHolder: (viewType: Int) -> DataBindingViewHolder<T, out ViewDataBinding>
) : SimpleDataAdapter<T>(context, data, createHolder), ICheckedParent<T> {

    constructor(
        context: Context,
        data: com.outs.core.android.databinding.data.source.DataSource<T> = com.outs.core.android.databinding.data.source.DataSource.empty(),
        onCheckChanged: ((T?, Int, Boolean) -> Unit)? = null,
        checked: HashSet<T> = HashSet(),
        itemLayoutId: Int
    ) : this(
        context,
        data,
        onCheckChanged,
        checked,
        createHolder = { CheckedAutoItem(itemLayoutId) })

    override fun onCreatedViewHolder(
        holder: AbsViewHolder<T>,
        itemHolder: DataBindingViewHolder<T, *>
    ) {
        super.onCreatedViewHolder(holder, itemHolder)
        if (itemHolder is CheckableViewHolder<T, *>) {
            itemHolder.checkedParent = this
        }
    }

    override fun isChecked(item: T): Boolean = checked.contains(item)

    override fun onDataSetChanged() {
        clearChecked()
        super.onDataSetChanged()
    }

    fun clearChecked() {
        val old = checked.toList()
        checked.clear()
        old.forEach {
            val position = notifyItemChanged(it)
            onCheckChanged?.invoke(it, position, false)
        }
    }

    fun checkAll() {
        data.data.filterNot(::isChecked)
            .onEach(checked::toggle)
            .forEach { new ->
                val position = notifyItemChanged(new)
                onCheckChanged?.invoke(new, position, true)
            }
    }

    override fun updateChecked(new: T?, withCallback: Boolean) {
        if (null != new) {
            val isChecked = isChecked(new)
            if (isChecked) {
                checked.remove(new)
            } else {
                checked.add(new)
            }
            val position = notifyItemChanged(new)
            if (withCallback) onCheckChanged?.invoke(new, position, !isChecked)
        }
    }

}