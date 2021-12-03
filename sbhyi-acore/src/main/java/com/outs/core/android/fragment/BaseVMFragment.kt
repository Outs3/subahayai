package com.outs.core.android.fragment

import android.view.LayoutInflater
import com.outs.core.android.config.ProgressStatus
import com.outs.core.android.vm.BaseViewModel

/**
 * author: Outs3
 * e-mail: 3.3nosekai@gmail.com
 * date: 2021/11/2 14:39
 * desc:
 */
abstract class BaseVMFragment<VM : BaseViewModel> : AbsFragment() {

    protected lateinit var mViewModel: VM

    protected open val bindLifecycleToViewModel: Boolean = true

    protected abstract val getViewModel: VM

    override fun initView(inflater: LayoutInflater) {
        mViewModel = getViewModel
        if (bindLifecycleToViewModel) {
            lifecycle.addObserver(mViewModel)
        }
        mViewModel.onClick = this
        mViewModel.progressStatus.observe(owner) { status ->
            when (status) {
                ProgressStatus.SHOW_PROGRESS -> {
                    mLoading.show()
                }
                ProgressStatus.CANCEL_PROGRESS -> {
                    mLoading.hide()
                }
            }
        }
        observe(mViewModel)
    }

    open fun observe(vm: VM) {
    }

}