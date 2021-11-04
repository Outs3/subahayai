package com.outs.core.android.databinding.adapter

import android.content.Context
import android.view.ViewGroup
import androidx.core.util.Supplier
import androidx.recyclerview.widget.RecyclerView
import java.util.*

abstract class CommonAdapter<T> : RecyclerView.Adapter<CommonAdapter.AbsViewHolder<T>>,
    IAdapter<T> {
    private var mDataList: MutableList<T> = ArrayList()
    private var mType: Any? = null  // item 类型
    private val mUtil = ItemTypeUtil()
    private var currentPos = 0

    constructor() : super()

    constructor(data: MutableList<T>) : this() {
        mDataList = data
    }

    override fun getItemCount(): Int {
        return mDataList.size
    }

    override fun setData(data: MutableList<T>) {
        mDataList = data
        notifyDataSetChanged()
    }

    open fun addList(list: MutableList<T>?) {
        if (list == null) return
        for (t in list) {
            mDataList.add(t)
        }
        notifyDataSetChanged()
    }

    override fun getData(): MutableList<T> {
        return mDataList
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    /**
     * instead by[.getItemType]
     *
     *
     * 通过数据得到obj的类型的type
     * 然后，通过[ItemTypeUtil]来转换位int类型的type
     */
    @Deprecated("")
    override fun getItemViewType(position: Int): Int {
        currentPos = position
        mType = getItemType(mDataList[position])
        return mUtil.getIntType(mType!!)
    }

    override fun getItemType(t: T): Any {
        return -1 // default
    }

    /**
     * 绑定视图
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbsViewHolder<T> {
        return AbsViewHolder(parent.context, parent, createItem(mType!!))
    }

    /**
     * 绑定数据
     */
    override fun onBindViewHolder(holder: AbsViewHolder<T>, position: Int) {
        holder.item.handleData(mDataList[position], position)
    }

    override fun getCurrentPosition(): Int {
        return currentPos
    }

    override fun onViewAttachedToWindow(holder: AbsViewHolder<T>) {
        super.onViewAttachedToWindow(holder)
        holder.item.onAttach()
    }

    override fun onViewDetachedFromWindow(holder: AbsViewHolder<T>) {
        super.onViewDetachedFromWindow(holder)
        holder.item.onDetach()
    }

    class AbsViewHolder<T>(context: Context, parent: ViewGroup, val item: AdapterItem<T>) :
        RecyclerView.ViewHolder(item.getItemView(context, parent)) {

        init {
            this.item.setViews()
        }
    }

    companion object {
        fun <T> simpleAdapter(
            data: ArrayList<T>? = null,
            generateViewHolder: Supplier<BaseViewHolder<T>>
        ): CommonAdapter<T> = object : CommonAdapter<T>() {
            override fun createItem(type: Any): AdapterItem<T> {
                return generateViewHolder.get()
            }
        }.apply {
            if (null != data && data.isNotEmpty()) setData(data)
        }
    }

}