package com.outs.core.android.databinding.dialog

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.outs.core.android.databinding.R
import com.outs.core.android.databinding.databinding.DialogBaseBinding
import com.outs.core.android.dialog.BaseSheetFragment

/**
 * author: Outs3
 * e-mail: 3.3nosekai@gmail.com
 * date: 2021/6/13 16:36
 * desc:
 */
abstract class BaseBottomFragment<VDB : ViewDataBinding> : BaseSheetFragment() {

    protected lateinit var mBaseBinding: DialogBaseBinding

    protected lateinit var mBinding: VDB

    protected abstract val getLayoutId: Int

    override val getContentView: View
        get() = mBaseBinding.root

    override fun initView(context: Context) {
        val inflater = LayoutInflater.from(context)
        mBaseBinding = DataBindingUtil.inflate(inflater, R.layout.dialog_base, null, false)
        mBinding = DataBindingUtil.inflate(
            inflater,
            getLayoutId,
            mBaseBinding.layoutContent,
            true
        )
        mBaseBinding.lifecycleOwner = this
        mBinding.lifecycleOwner = this
    }

}

