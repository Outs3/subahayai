package com.outs.core.android.databinding.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.outs.core.android.activity.BaseVMActivity
import com.outs.core.android.databinding.R
import com.outs.core.android.databinding.databinding.ActivityBaseBinding
import com.outs.core.android.databinding.debug.DebugLogActivity
import com.outs.core.android.databinding.dialog.BottomSelectDialog
import com.outs.core.android.vm.BaseViewModel
import com.outs.utils.android.isDebug

abstract class BaseActivity<VDB : ViewDataBinding, VM : BaseViewModel> : BaseVMActivity<VM>() {

    protected lateinit var mBaseBinding: ActivityBaseBinding
    protected lateinit var mBinding: VDB

    protected abstract val getLayoutId: Int

    private val mDebugOpts: ArrayList<Pair<String, () -> Unit>> by lazy { getDebugOpts() }

    protected open val isEnableDebug: Boolean
        get() = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (isDebug && isEnableDebug) {
            onDebug()
        }
    }

//    override fun showLoading() {
//        if (FrameworkConfig.useLocalLoading) {
//            mBaseBinding.layoutLoading.visible()
//        } else {
//            mLoading.show()
//        }
//    }
//
//    override fun hideLoading() {
//        if (FrameworkConfig.useLocalLoading) {
//            mBaseBinding.layoutLoading.gone()
//        } else {
//            mLoading.hide()
//        }
//    }

    override fun initView() {
        mBaseBinding = DataBindingUtil.setContentView(this, R.layout.activity_base)
        mBinding = DataBindingUtil.inflate(
            LayoutInflater.from(this),
            getLayoutId,
            mBaseBinding.layoutContent,
            true
        )
        mBaseBinding.lifecycleOwner = this
        mBinding.lifecycleOwner = this
    }

    open fun onDebug() {
        mBaseBinding.fab.visibility = View.VISIBLE
        mBaseBinding.fab.setOnClickListener { view ->
            val debugOpts = mDebugOpts
            BottomSelectDialog()
                .content(debugOpts.map { it.first })
                .onConfirm { dialog, position, text ->
                    dialog.dismiss()
                    debugOpts[position].second()
                }
                .show(supportFragmentManager)
        }
        addDebugOpt("debug log" to { DebugLogActivity.start(context) })
    }

    open fun getDebugOpts(): ArrayList<Pair<String, () -> Unit>> = arrayListOf()

    fun addDebugOpt(vararg opts: Pair<String, () -> Unit>) = mDebugOpts.addAll(opts)

}