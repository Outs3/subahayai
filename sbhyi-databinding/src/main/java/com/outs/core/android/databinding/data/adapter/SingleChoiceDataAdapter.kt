package com.outs.core.android.databinding.data.adapter

import android.content.Context
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.outs.core.android.databinding.holder.CheckableViewHolder
import com.outs.core.android.databinding.holder.CheckedAutoItem
import com.outs.core.android.databinding.holder.DataBindingViewHolder
import com.outs.core.android.databinding.holder.ICheckedParent
import com.outs.utils.android.setOnNot

/**
 * author: Outs3
 * e-mail: 3.3nosekai@gmail.com
 * date: 2021/5/7 10:24
 * desc:
 */
open class SingleChoiceDataAdapter<T : Any>(
    context: Context,
    data: com.outs.core.android.databinding.data.source.DataSource<T> = com.outs.core.android.databinding.data.source.DataSource.empty(),
    var onCheckChanged: ((T?, Int) -> Unit)? = null,
    var checked: T? = null,
    createHolder: (viewType: Int) -> DataBindingViewHolder<T, out ViewDataBinding>
) : SimpleDataAdapter<T>(context, data, createHolder), ICheckedParent<T> {

    constructor(
        context: Context,
        data: com.outs.core.android.databinding.data.source.DataSource<T> = com.outs.core.android.databinding.data.source.DataSource.empty(),
        onCheckChanged: ((T?, Int) -> Unit)? = null,
        checked: T? = null,
        itemLayoutId: Int
    ) : this(
        context,
        data,
        onCheckChanged,
        checked,
        createHolder = { CheckedAutoItem(itemLayoutId) })

    constructor(
        context: Context,
        data: List<T> = emptyList(),
        onCheckChanged: ((T?, Int) -> Unit)? = null,
        checked: T? = null,
        itemLayoutId: Int
    ) : this(
        context,
        com.outs.core.android.databinding.data.source.DataSource.fromIterable(data),
        onCheckChanged,
        checked,
        createHolder = { CheckedAutoItem(itemLayoutId) })

    constructor(
        context: Context,
        data: com.outs.core.android.databinding.data.source.DataSource<T> = com.outs.core.android.databinding.data.source.DataSource.empty(),
        lifecycleOwner: LifecycleOwner,
        liveChecked: MutableLiveData<T>,
        itemLayoutId: Int
    ) : this(
        context,
        data,
        { checked, position -> liveChecked.setOnNot(checked) },
        liveChecked.value,
        createHolder = { CheckedAutoItem(itemLayoutId) }) {
        liveChecked.observe(lifecycleOwner) {
            updateChecked(it, false)
        }
    }

    var position: Int = -1

    override fun onCreatedViewHolder(
        holder: AbsViewHolder<T>,
        itemHolder: DataBindingViewHolder<T, out ViewDataBinding>
    ) {
        super.onCreatedViewHolder(holder, itemHolder)
        if (itemHolder is CheckableViewHolder<T, *>) {
            itemHolder.checkedParent = this
        }
    }

    override fun isChecked(item: T): Boolean = checked == item

    override fun onDataSetChanged() {
        updateChecked(null)
        super.onDataSetChanged()
    }

    fun updateCheckedByPosition(position: Int) {
        updateChecked(data.data.getOrNull(position))
    }

    override fun updateChecked(new: T?, withCallback: Boolean) {
        val old = checked

        if (old == new) return
        checked = new

        if (null != old) {
            notifyItemChanged(old)
        }

        if (null != new) {
            val position = notifyItemChanged(new)
            this.position = position
            if (withCallback) onCheckChanged(new, position)
        }
    }

    private fun onCheckChanged(new: T?, position: Int) {
        onCheckChanged?.invoke(new, position)
    }

}