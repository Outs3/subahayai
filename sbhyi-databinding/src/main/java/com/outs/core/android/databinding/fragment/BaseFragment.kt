package com.outs.core.android.databinding.fragment

import android.view.LayoutInflater
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.outs.core.android.fragment.BaseVMFragment
import com.outs.core.android.vm.BaseViewModel
import com.outs.utils.android.isDebug

/**
 * author: Outs3
 * e-mail: 3.3nosekai@gmail.com
 * date: 2021/4/27 13:43
 * desc:
 */
abstract class BaseFragment<VDB : ViewDataBinding, VM : BaseViewModel> : BaseVMFragment<VM>() {

    protected lateinit var mBinding: VDB

    protected abstract val getLayoutId: Int

    override val getContentView: View
        get() = mBinding.root

    override fun initView(inflater: LayoutInflater) {
        mBinding = DataBindingUtil.inflate(
            inflater,
            getLayoutId,
            null,
            true
        )
        mBinding.lifecycleOwner = this
        super.initView(inflater)
        if (isDebug) {
            onDebug()
        }
    }

    open fun onDebug() {

    }

}