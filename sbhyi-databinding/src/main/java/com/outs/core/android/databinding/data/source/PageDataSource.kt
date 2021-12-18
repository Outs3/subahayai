package com.outs.core.android.databinding.data.source

import androidx.lifecycle.MutableLiveData
import com.outs.utils.kotlin.emptyAction

/**
 * author: Outs3
 * e-mail: 3.3nosekai@gmail.com
 * date: 2021/4/9 17:00
 * desc:
 */
abstract class PageDataSource<T> : ListDataSource<T>() {

    //当前页码
    protected var mPage: Int = 1

    //每页数据量
    protected var mPageSize: Int = 20

    //每次加载新数据都清除旧数据（翻页）
    var clearOnLoad: Boolean = false

    //没有更多数据
    val isNoMore = MutableLiveData<Boolean>()

    override fun refresh(onComplete: () -> Unit) {
        mPage = 1
        super.refresh(onComplete)
    }

    open fun loadMore(onComplete: () -> Unit = emptyAction) {
        mPage++
        getList(onComplete)
    }

    override fun loadList(data: MutableList<T>) {
        val needClear = clearOnLoad || 1 == mPage
        if (needClear)
            this.data.clear()
        val count = data.size
        isNoMore.postValue(count < mPageSize)
        if (data.isNotEmpty()) {
            val position = this.data.size
            this.data.addAll(data)
            if (needClear) {
                notifyAdapters()
            } else {
                notifyAdapters { it.notifyItemRangeInserted(position, count) }
            }
        } else if (needClear) {
            notifyAdapters()
        }
    }

}