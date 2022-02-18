package com.outs.demo_databinding.ui

import android.os.Bundle
import com.outs.core.android.databinding.activity.BaseActivity
import com.outs.demo_databinding.R
import com.outs.demo_databinding.databinding.ActivityInjectBinding
import com.outs.utils.android.intent.Extra
import com.outs.utils.android.viewModel

/**
 * author: Outs3
 * e-mail: 3.3nosekai@gmail.com
 * date: 2022/2/18 9:22
 * desc:
 */
class InjectActivity : BaseActivity<ActivityInjectBinding, InjectViewModel>() {

    @Extra
    var iInt: Int = 0

    @Extra
    var iInteger: Int? = 0

    override val getViewModel: InjectViewModel
        get() = viewModel()

    override val getLayoutId: Int
        get() = R.layout.activity_inject

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding.text.text = "iInt: $iInt, iInteger: $iInteger"
    }

}