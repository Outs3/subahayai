package com.outs.core.android.databinding.debug

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.outs.core.android.databinding.R
import com.outs.core.android.databinding.activity.BaseActivity
import com.outs.core.android.databinding.data.adapter.SimpleDataAdapter
import com.outs.core.android.databinding.databinding.ActivityDebugLogBinding
import com.outs.utils.android.clickOrRepeat
import com.outs.utils.android.startActivity
import com.outs.utils.android.viewModel

/**
 * author: Outs3
 * e-mail: 3.3nosekai@gmail.com
 * date: 2021/10/8 10:56
 * desc:
 */
open class DebugLogActivity : BaseActivity<ActivityDebugLogBinding, DebugLogViewModel>() {

    companion object {
        fun start(context: Context) = context.startActivity<DebugLogActivity>()
    }

    private val logDataAdapter by lazy {
        SimpleDataAdapter(
            context,
            mViewModel.logData,
            itemLayoutId = R.layout.item_log_msg
        )
    }

    override val isEnableDebug: Boolean
        get() = false

    override val getLayoutId: Int
        get() = R.layout.activity_debug_log

    override val getViewModel: DebugLogViewModel
        get() = viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding.onClick = this
        mBinding.viewModel = mViewModel
        logDataAdapter.bind(
            owner,
            mBinding.recycler,
            layoutManager = LinearLayoutManager(
                context,
                LinearLayoutManager.VERTICAL,
                false
            ).apply {
                stackFromEnd = true
            }
        )
    }

    override fun onClick(v: View) {
        super.onClick(v)
        v.clickOrRepeat() ?: return
        when (v.id) {
            R.id.image_ctrl_up -> mBinding.recycler.smoothScrollToPosition(0)
            R.id.image_ctrl_down -> mBinding.recycler.smoothScrollToPosition(logDataAdapter.itemCount - 1)
        }
    }

}