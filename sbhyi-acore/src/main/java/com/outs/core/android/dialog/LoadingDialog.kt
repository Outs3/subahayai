package com.outs.core.android.dialog

import android.content.Context
import android.view.View
import com.outs.core.android.R
import com.outs.utils.android.inflate

/**
 * author: Outs3
 * e-mail: 3.3nosekai@gmail.com
 * date: 2021/11/2 13:58
 * desc:
 */
open class LoadingDialog : BaseDialogFragment() {

    private lateinit var root: View

    override val getContentView: View
        get() = root

    override val canCancel: Boolean
        get() = false

    override fun initView(context: Context) {
        root = R.layout.dialog_loading.inflate(context)
    }

    companion object {
        const val TAG = "LoadingDialog"
    }
}