package com.outs.demo_databinding.base

import android.os.Bundle
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.outs.demo_databinding.R
import com.outs.demo_databinding.databinding.ActivityListBinding
import com.outs.demo_databinding.databinding.IncludeToolbarBinding
import com.outs.core.android.databinding.data.adapter.SimpleDataAdapter
import com.outs.core.android.databinding.holder.AutoItem
import com.outs.core.android.databinding.holder.DataBindingViewHolder
import com.outs.core.android.databinding.holder.OnModelClickListener


/**
 * author: Outs3
 * e-mail: 3.3nosekai@gmail.com
 * date: 2021/5/13 14:15
 * desc:
 */
abstract class ListActivity<T : Any, LVM : ListViewModel<T>> :
    ToolbarActivity<ActivityListBinding, LVM>(),
    OnModelClickListener<T> {

    protected open val adapter: SimpleDataAdapter<T> by lazy {
        SimpleDataAdapter(
            context,
            mViewModel.data,
            ::createItemHolder
        )
    }

    protected open val refreshOnCreate: Boolean = true

    protected open val refreshOnResume: Boolean = false

    protected open val getLayoutManager: RecyclerView.LayoutManager =
        LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

    protected open fun createItemHolder(viewType: Int): DataBindingViewHolder<T, out ViewDataBinding> =
        AutoItem(getItemLayoutId, this)

    abstract val getItemLayoutId: Int

    override val getToolbar: IncludeToolbarBinding
        get() = mBinding.includeToolbar

    override val getLayoutId: Int
        get() = R.layout.activity_list

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter.bind(
            owner,
            mBinding.includeRefresh.recycler,
            mBinding.includeRefresh.refresh,
            getLayoutManager,
            viewModel = mViewModel,
            refreshOnCreate = refreshOnCreate,
            refreshOnResume = refreshOnResume
        )
    }

    override fun onModelClick(position: Int, model: T, view: View?) {

    }

}