package com.outs.demo_databinding.base

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import com.outs.demo_databinding.R
import com.outs.demo_databinding.databinding.IncludeToolbarBinding
import com.outs.core.android.databinding.fragment.BaseFragment
import com.outs.core.android.vm.BaseViewModel
import com.outs.utils.kotlin.typeOfOrNull

/**
 * author: Outs3
 * e-mail: 3.3nosekai@gmail.com
 * date: 2021/8/13 10:01
 * desc:
 */
abstract class ToolbarFragment<VDB : ViewDataBinding, VM : BaseViewModel> :
    BaseFragment<VDB, VM>() {

    protected lateinit var mToolbarBinding: IncludeToolbarBinding

    abstract val getTitle: String

    abstract val getToolbar: IncludeToolbarBinding

    open val setSupportActionBar: Boolean
        get() = true

    override fun initView() {
        super.initView()
        initToolbar()
    }

    protected open fun initToolbar() {
        mToolbarBinding = getToolbar
        mToolbarBinding.onClick = this
        mToolbarBinding.toolbar.title = ""
        mToolbarBinding.textTitle.text = getTitle
        if (setSupportActionBar) {
            activity?.typeOfOrNull<AppCompatActivity>()
                ?.setSupportActionBar(mToolbarBinding.toolbar)
        }
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when (v.id) {
            R.id.image_back -> {
                parentFragmentManager.popBackStack()
            }
        }
    }
}