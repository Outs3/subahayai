package com.outs.core.android.databinding.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes

interface AdapterItem<T> {

    /**
     * @return item布局文件的layoutId
     */
    @get:LayoutRes
    val getLayoutId: Int

    /**
     * 初始化views
     */
    fun getItemView(context: Context, parent: ViewGroup?): View


    /**
     * 设置view的参数
     */
    fun setViews()

    /**
     * 根据数据来设置item的内部views
     *
     * @param model    数据list内部的model
     * @param position 当前adapter调用item的位置
     */
    fun handleData(model: T?, position: Int)

    /**
     * Item绑定到视图
     */
    fun onAttach()

    /**
     * Item从视图解绑
     */
    fun onDetach()
}
