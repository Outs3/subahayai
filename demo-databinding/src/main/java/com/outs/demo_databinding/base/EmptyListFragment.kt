package com.outs.demo_databinding.base

import android.view.View
import com.outs.utils.android.viewModel

/**
 * author: Outs3
 * e-mail: 3.3nosekai@gmail.com
 * date: 2021/9/7 16:17
 * desc:
 */
class EmptyListFragment : ListFragment<Unit, EmptyListViewModel>() {

    override val getItemLayoutId: Int
        get() = com.outs.core.android.databinding.R.layout.empty_view

    override val getViewModel: EmptyListViewModel
        get() = viewModel()

    override fun onModelClick(position: Int, model: Unit, view: View?) {
    }

    override fun observe(vm: EmptyListViewModel) {

    }

}