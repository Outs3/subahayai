package com.outs.core.android.databinding.activity

import com.outs.core.android.databinding.R
import com.outs.core.android.databinding.databinding.ContentRecyclerViewBinding
import com.outs.core.android.vm.BaseViewModel

abstract class RecyclerActivity<VM : BaseViewModel> :
    BaseActivity<ContentRecyclerViewBinding, VM>() {

    override val getLayoutId: Int
        get() = R.layout.content_recycler_view

}