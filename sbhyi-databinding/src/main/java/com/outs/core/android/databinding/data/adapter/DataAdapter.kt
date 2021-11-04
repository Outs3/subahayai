package com.outs.core.android.databinding.data.adapter

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.outs.core.android.vm.BaseViewModel
import com.outs.utils.android.lifecycle.AutoCloseLifecycleObserver
import com.scwang.smart.refresh.layout.SmartRefreshLayout

/**
 * author: Outs3
 * e-mail: 3.3nosekai@gmail.com
 * date: 2021/6/13 16:36
 * desc:
 */
abstract class DataAdapter<T, VH : RecyclerView.ViewHolder>(
    val data: com.outs.core.android.databinding.data.source.DataSource<T>
) :
    com.outs.core.android.databinding.adapter.BaseAdapter<VH>() {

    private val refreshObserver = RefreshObserver()

    var refreshOnCreate: Boolean = false
    var refreshOnResume: Boolean = false

    override fun getItemCount(): Int = data.data.size

    open fun bind(
        lifecycleOwner: LifecycleOwner? = null,
        recyclerView: RecyclerView,
        refreshLayout: SmartRefreshLayout? = null,
        layoutManager: RecyclerView.LayoutManager? = null,
        itemDecoration: RecyclerView.ItemDecoration? = null,
        viewModel: BaseViewModel? = null,
        refreshOnCreate: Boolean = false,
        refreshOnResume: Boolean = false,
        enableRefresh: Boolean = true,
        enableLoadMore: Boolean = true,
        onRefresh: () -> Unit = {},
        onLoadMore: () -> Unit = {}
    ) {
        bindToViewModel(lifecycleOwner, viewModel)
        bindToData(lifecycleOwner?.lifecycle)
        bindToLifecycle(lifecycleOwner?.lifecycle, refreshOnCreate, refreshOnResume)
        bindToRecycler(recyclerView, layoutManager, itemDecoration)
        bindToRefresh(
            lifecycleOwner,
            refreshLayout,
            enableRefresh,
            enableLoadMore,
            onRefresh,
            onLoadMore
        )
    }

    open fun bindToViewModel(
        lifecycleOwner: LifecycleOwner? = null,
        viewModel: BaseViewModel? = null
    ) {
        if (null != lifecycleOwner && null != viewModel) {
            data.isEmpty.observe(lifecycleOwner, viewModel.isEmpty::postValue)
        }
    }

    open fun bindToData(lifecycle: Lifecycle? = null) {
        data.addAdapter(this, lifecycle)
    }

    open fun bindToLifecycle(
        lifecycle: Lifecycle? = null,
        refreshOnCreate: Boolean? = null,
        refreshOnResume: Boolean? = null
    ) {
        refreshOnCreate?.let { this.refreshOnCreate = it }
        refreshOnResume?.let { this.refreshOnResume = it }
        lifecycle?.addObserver(refreshObserver)
    }

    open fun bindToRecycler(
        recyclerView: RecyclerView,
        layoutManager: RecyclerView.LayoutManager? = null,
        itemDecoration: RecyclerView.ItemDecoration? = null,
    ) {
        if (null != layoutManager) {
            if (recyclerView.layoutManager != layoutManager) {
                recyclerView.layoutManager = layoutManager
            }
        } else if (null == recyclerView.layoutManager) {
            recyclerView.layoutManager = LinearLayoutManager(com.outs.utils.android.appInstance)
        }
        recyclerView.adapter = this
        itemDecoration?.let { recyclerView.addItemDecoration(it) }
    }

    open fun bindToRefresh(
        lifecycleOwner: LifecycleOwner? = null,
        refreshLayout: SmartRefreshLayout? = null,
        enableRefresh: Boolean = true,
        enableLoadMore: Boolean = true,
        onRefresh: () -> Unit = {},
        onLoadMore: () -> Unit = {}
    ) {
        refreshLayout?.apply {
            when (data) {
                is com.outs.core.android.databinding.data.source.PageDataSource -> {
                    setEnableRefresh(enableRefresh)
                    setEnableLoadMore(enableLoadMore)
                    if (enableLoadMore) {
                        lifecycleOwner?.let { owner ->
                            data.isNoMore.observe(owner, { setEnableLoadMore(!it) })
                        }
                    }
                    setOnRefreshListener { refreshLayout ->
                        onRefresh()
                        data.refresh { refreshLayout.finishRefresh() }
                    }
                    setOnLoadMoreListener { refreshLayout ->
                        onLoadMore()
                        data.loadMore { refreshLayout.finishLoadMore() }
                    }
                }
                is com.outs.core.android.databinding.data.source.ListDataSource -> {
                    setEnableRefresh(enableRefresh)
                    setEnableLoadMore(false)
                    setOnRefreshListener { refreshLayout ->
                        onRefresh()
                        data.refresh { refreshLayout.finishRefresh() }
                    }
                }
                else -> {
                    setEnableLoadMore(false)
                    setEnableRefresh(false)
                }
            }
        }
    }

    open fun onDataSetChanged() {
        notifyDataSetChanged()
    }

    open fun notifyItemChanged(item: T?): Int =
        item?.let { data.data.indexOf(it) }?.also { if (-1 != it) notifyItemChanged(it) } ?: -1

    private inner class RefreshObserver : AutoCloseLifecycleObserver {
        override fun onCreate(owner: LifecycleOwner) {
            super.onCreate(owner)
            if (refreshOnCreate && data is com.outs.core.android.databinding.data.source.ListDataSource) data.refresh()
        }

        override fun onResume(owner: LifecycleOwner) {
            super.onResume(owner)
            if (refreshOnResume && data is com.outs.core.android.databinding.data.source.ListDataSource) data.refresh()
        }
    }

}