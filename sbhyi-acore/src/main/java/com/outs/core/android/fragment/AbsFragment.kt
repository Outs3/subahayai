package com.outs.core.android.fragment

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import com.outs.core.android.R
import com.outs.utils.android.injectArgs

/**
 * author: Outs3
 * e-mail: 3.3nosekai@gmail.com
 * date: 2021/11/2 14:33
 * desc:
 */
abstract class AbsFragment : Fragment(), View.OnClickListener {

    protected abstract val getContentView: View

    protected val owner: LifecycleOwner = this

    protected val mLoading by lazy {
        Dialog(requireContext(), R.style.dialog_style).apply {
            setContentView(R.layout.dialog_loading)
            setCancelable(false)
            setCanceledOnTouchOutside(false)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        injectArgs()
        initView(inflater)
        initView()
        return getContentView
    }

    protected abstract fun initView(inflater: LayoutInflater)

    open fun initView() {
        //tryOr { mBinding.func<Unit>("setViewModel", true, mViewModel) }
    }

    override fun onClick(v: View) {

    }

}