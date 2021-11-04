package com.outs.core.android.databinding.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

abstract class SimpleItem<T> : AdapterItem<T> {

    protected lateinit var rootView: View
    protected var model: T? = null
    protected var position = 0

    /**
     * 获取布局
     */
    override fun getItemView(context: Context, parent: ViewGroup?): View {
        rootView = LayoutInflater.from(context).inflate(getLayoutId, parent, false)
        return rootView
    }

    /**
     * 设置视图
     *
     * 可在这里面添加点击事件
     */
    override fun setViews() {}

    /**
     * Item绑定到视图
     */
    override fun onAttach() {}

    /**
     * Item从视图解绑
     */
    override fun onDetach() {}

    override fun handleData(model: T?, position: Int) {
        this.model = model
        this.position = position
    }
}
