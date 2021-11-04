package com.outs.core.android.activity

import android.os.Bundle
import com.outs.core.android.config.ProgressStatus
import com.outs.core.android.vm.BaseViewModel

/**
 * author: Outs3
 * e-mail: 3.3nosekai@gmail.com
 * date: 2021/11/2 13:49
 * desc:
 */
abstract class BaseVMActivity<VM : BaseViewModel> : AbsActivity() {

    protected lateinit var mViewModel: VM

    protected abstract val getViewModel: VM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel = getViewModel
        lifecycle.addObserver(mViewModel)
        mViewModel.onClick = this
        mViewModel.bindToSelf()
        observe(mViewModel)
    }

    fun BaseViewModel.bindToSelf() {
        progressStatus.observe(owner) { status ->
            when (status) {
                ProgressStatus.SHOW_PROGRESS -> {
                    showLoading()
                }
                ProgressStatus.CANCEL_PROGRESS -> {
                    hideLoading()
                }
                null -> {

                }
            }
        }
    }

    open fun observe(vm: VM) {
    }

}