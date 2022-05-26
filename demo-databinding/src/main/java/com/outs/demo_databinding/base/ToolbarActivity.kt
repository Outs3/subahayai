package com.outs.demo_databinding.base

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.databinding.ViewDataBinding
import com.outs.core.android.databinding.activity.BaseActivity
import com.outs.core.android.vm.BaseViewModel
import com.outs.demo_databinding.R
import com.outs.demo_databinding.databinding.IncludeToolbarBinding

/**
 * author: Outs3
 * e-mail: 3.3nosekai@gmail.com
 * date: 2021/8/1 11:56
 * desc:
 */
abstract class ToolbarActivity<VDB : ViewDataBinding, VM : BaseViewModel> :
    BaseActivity<VDB, VM>() {

    protected lateinit var mToolbarBinding: IncludeToolbarBinding

    abstract val getTitle: String

    abstract val getToolbar: IncludeToolbarBinding

    open val getToolbarColor: Int
        get() = Color.TRANSPARENT

    open val isToolbarDark: Boolean
        get() = false

    open val syncStatusBarColor: Boolean
        get() = true

    open val setSupportActionBar: Boolean
        get() = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initToolbar()
    }

    protected open fun initToolbar() {
        mToolbarBinding = getToolbar
        mToolbarBinding.onClick = this
        mToolbarBinding.toolbar.title = ""
        mToolbarBinding.textTitle.text = getTitle
        val toolbarColor = getToolbarColor
        if (syncStatusBarColor) {
            if (Color.TRANSPARENT != toolbarColor) {
                mToolbarBinding.root.setBackgroundColor(toolbarColor)
                window.statusBarColor = toolbarColor
            }
            if (isToolbarDark) {
                mToolbarBinding.textTitle.setTextColor(Color.WHITE)
                mToolbarBinding.imageBack.imageTintList = ColorStateList.valueOf(Color.WHITE)
            } else {
                mToolbarBinding.imageMenuShare.imageTintList =
                    ColorStateList.valueOf(
                        ContextCompat.getColor(
                            context,
                            com.outs.core.android.R.color.black_333
                        )
                    )
            }
        }
        if (setSupportActionBar) {
            setSupportActionBar(mToolbarBinding.toolbar)
        }
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when (v.id) {
            R.id.image_back -> {
                finish()
            }
        }
    }
}