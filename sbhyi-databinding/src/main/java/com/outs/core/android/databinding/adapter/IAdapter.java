package com.outs.core.android.databinding.adapter;


import androidx.annotation.Keep;
import androidx.annotation.NonNull;

import java.util.List;

public interface IAdapter<T> {

    /**
     * 获取数据源
     *
     * @return
     */
    List<T> getData();

    /**
     * @param data 设置数据源
     */
    void setData(@NonNull List<T> data);

    /**
     * @param t list中的一条数据
     * @return 强烈建议返回string, int, bool类似的基础对象做type，不要返回data中的某个对象
     */
    @NonNull
    Object getItemType(T t);

    /**
     * 当缓存中无法得到所需item时才会调用
     *
     * @param type 通过{@link #getItemType(Object)}得到的type
     * @return 任意类型的 AdapterItem
     */
    @Keep
    @NonNull
    AdapterItem<T> createItem(@NonNull Object type);

    /**
     * 得到当前要渲染的最后一个item的position
     */
    int getCurrentPosition();
}
