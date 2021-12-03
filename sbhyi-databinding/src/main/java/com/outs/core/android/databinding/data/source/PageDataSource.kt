package com.outs.core.android.databinding.data.source

import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.outs.utils.kotlin.emptyAction

/**
 * author: Outs3
 * e-mail: 3.3nosekai@gmail.com
 * date: 2021/4/9 17:00
 * desc:
 */
abstract class PageDataSource<T> : ListDataSource<T>() {

    protected var mPage: Int = 1
    protected var mPageSize: Int = 20

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
        val needClear = 1 == mPage
        if (needClear)
            this.data.clear()
        if (data.isNotEmpty()) {
            val position = this.data.size
            val count = data.size
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

    override fun notifyAdapters(action: (RecyclerView.Adapter<*>) -> Unit) {
        isNoMore.postValue(this.data.size < mPage * mPageSize)
        super.notifyAdapters(action)
    }

}