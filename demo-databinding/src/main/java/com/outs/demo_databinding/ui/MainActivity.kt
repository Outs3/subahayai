package com.outs.demo_databinding.ui

import com.outs.core.android.databinding.activity.BaseActivity
import com.outs.demo_databinding.R
import com.outs.demo_databinding.databinding.ActivityMainBinding
import com.outs.utils.android.viewModel

/**
 * author: Outs3
 * e-mail: 3.3nosekai@gmail.com
 * date: 2021/11/4 14:39
 * desc:
 */
class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>() {

    override val getViewModel: MainViewModel
        get() = viewModel()

    override val getLayoutId: Int
        get() = R.layout.activity_main

}