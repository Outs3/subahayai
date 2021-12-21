package com.outs.core.android.databinding.data.adapter

import android.content.Context
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.outs.core.android.databinding.data.source.DataSource
import com.outs.core.android.databinding.data.source.DataSourceFactory
import com.outs.core.android.databinding.holder.DataBindingViewHolder
import com.outs.core.android.databinding.holder.OnModelClickListener
import java.lang.ref.WeakReference

/**
 * author: Outs3
 * e-mail: 3.3nosekai@gmail.com
 * date: 2021/6/13 16:36
 * desc:
 */
open class SimpleDataAdapter<T : Any>(
    val context: Context,
    data: DataSource<T> = DataSourceFactory.empty(),
    val createHolder: ICreateHolder<T>
) : DataAdapter<T, SimpleDataAdapter.AbsViewHolder<T>>(data) {

    constructor(
        context: Context,
        data: Iterable<T>,
        createHolder: (viewType: Int) -> DataBindingViewHolder<T, out ViewDataBinding>
    ) : this(
        context,
        DataSourceFactory.fromIterable(data),
        createHolder
    )

    constructor(
        context: Context,
        data: DataSource<T> = DataSourceFactory.empty(),
        itemLayoutId: Int,
        onModelClick: OnModelClickListener<T>? = null
    ) : this(context, data, CreateHolderFactory.auto(itemLayoutId, onModelClick))

    constructor(
        context: Context,
        data: Iterable<T>,
        itemLayoutId: Int,
        onModelClick: OnModelClickListener<T>? = null
    ) : this(context, data, CreateHolderFactory.auto(itemLayoutId, onModelClick))

    open fun onCreatedViewHolder(
        holder: AbsViewHolder<T>,
        itemHolder: DataBindingViewHolder<T, *>
    ) {
        itemHolder.mHolder = WeakReference(holder)
    }

    open fun createViewHolder(viewType: Int): DataBindingViewHolder<T, out ViewDataBinding> =
        createHolder(viewType)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbsViewHolder<T> =
        AbsViewHolder(context, parent, createViewHolder(viewType)).also {
            onCreatedViewHolder(it, it.item)
        }

    override fun onBindViewHolder(holder: AbsViewHolder<T>, position: Int) {
        val model = data.data[position]
        holder.item.handleData(model, position)
    }

    class AbsViewHolder<T>(
        context: Context,
        parent: ViewGroup,
        val item: DataBindingViewHolder<T, *>
    ) :
        RecyclerView.ViewHolder(item.getItemView(context, parent)) {

        init {
            this.item.setViews()
        }
    }
}