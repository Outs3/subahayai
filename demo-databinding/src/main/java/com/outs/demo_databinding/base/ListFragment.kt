package com.outs.demo_databinding.base

import android.content.Context
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.outs.demo_databinding.R
import com.outs.demo_databinding.databinding.FragmentListBinding
import com.outs.core.android.databinding.data.adapter.SimpleDataAdapter
import com.outs.core.android.databinding.data.source.DataSource
import com.outs.core.android.databinding.data.source.ListDataSource
import com.outs.core.android.databinding.fragment.BaseFragment
import com.outs.core.android.databinding.holder.AutoItem
import com.outs.core.android.databinding.holder.DataBindingViewHolder
import com.outs.core.android.databinding.holder.OnModelClickListener
import com.outs.core.android.databinding.visibleOrNot
import com.outs.utils.android.intent.Extra
import com.outs.utils.kotlin.typeOfOrNull

/**
 * author: Outs3
 * e-mail: 3.3nosekai@gmail.com
 * date: 2021/8/11 16:43
 * desc:
 */
abstract class ListFragment<T : Any, LVM : ListViewModel<T>> :
    BaseFragment<FragmentListBinding, LVM>(), OnModelClickListener<T> {

    @Extra
    var title: String = ""

    @Extra
    private var search: String = ""

    @Extra
    private var type: String = ""

    @Extra
    private var sort: String = ""

    protected open val adapter: SimpleDataAdapter<T> by lazy {
        createAdapter(
            requireContext(),
            mViewModel.data
        )
    }

    protected open val refreshOnCreate: Boolean = false

    protected open val refreshOnResume: Boolean = true

    protected open val getLayoutManager: RecyclerView.LayoutManager =
        LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

    protected open fun createAdapter(
        context: Context,
        dataSource: DataSource<T>
    ): SimpleDataAdapter<T> =
        SimpleDataAdapter(
            context,
            dataSource,
            ::createItemHolder
        )

    protected open fun createItemHolder(viewType: Int): DataBindingViewHolder<T, out ViewDataBinding> =
        AutoItem(getItemLayoutId, this)

    abstract val getItemLayoutId: Int

    override val getLayoutId: Int
        get() = R.layout.fragment_list

    override fun initView() {
        super.initView()
        mViewModel.also { vm ->
            vm.filterText.value = search
            vm.filterType.value = type
            vm.sortType.value = sort
        }
        mViewModel.isEmpty.observe(owner) {
            mBinding.includeRefresh.root.visibleOrNot(!it)
            mBinding.textEmpty.visibleOrNot(it)
        }
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

    open fun refresh() {
        if (isAdded) {
            mViewModel.data
                .takeIf(DataSource<*>::isBindToAdapter)
                ?.typeOfOrNull<ListDataSource<*>>()
                ?.refresh()
        }
    }

}