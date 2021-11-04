package com.outs.core.android.databinding.dialog

import android.content.Context
import android.view.View
import androidx.databinding.ViewDataBinding
import com.outs.core.android.databinding.R
import com.outs.core.android.databinding.databinding.DialogBaseBinding
import com.outs.core.android.databinding.inflateDataBinding

/**
 * author: Outs3
 * e-mail: 3.3nosekai@gmail.com
 * date: 2021/6/13 16:36
 * desc:
 */
abstract class BaseDialogFragment<VDB : ViewDataBinding> :
    com.outs.core.android.dialog.BaseDialogFragment() {

    protected lateinit var mBaseBinding: DialogBaseBinding

    protected lateinit var mBinding: VDB

    protected abstract val getLayoutId: Int

    override val getContentView: View
        get() = mBaseBinding.root

    override fun initView(context: Context) {
        mBaseBinding = R.layout.dialog_base.inflateDataBinding(context)
        mBinding = getLayoutId.inflateDataBinding(context, mBaseBinding.layoutContent, true)
        mBaseBinding.lifecycleOwner = this
        mBinding.lifecycleOwner = this
    }

}

