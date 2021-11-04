package com.outs.demo_databinding.dialog

import android.view.View
import android.view.ViewGroup
import com.outs.demo_databinding.R
import com.outs.demo_databinding.databinding.DialogConfirmBinding
import com.outs.core.android.databinding.dialog.BaseDialogFragment
import com.outs.utils.android.clickOrRepeat

/**
 * author: Outs3
 * e-mail: 3.3nosekai@gmail.com
 * date: 2021/8/20 17:33
 * desc:
 */
class ConfirmDialog : BaseDialogFragment<DialogConfirmBinding>() {

    private val defaultAction: (ConfirmDialog) -> Unit by lazy { { dismiss() } }
    private var title: String? = null
    private var text: String? = null
    private var confirmText: String? = null
    private var confirmColor: Int? = null
    private var cancelText: String? = null
    private var onConfirm: ((ConfirmDialog) -> Unit)? = null
    private var onCancel: ((ConfirmDialog) -> Unit)? = null

    override val getLayoutId: Int
        get() = R.layout.dialog_confirm

    override val getWidth: Int
        get() = ViewGroup.LayoutParams.MATCH_PARENT

    override fun initView() {
        super.initView()
        mBinding.onClick = this
        title?.let(mBinding.title::setText)
        text?.let(mBinding.text::setText)
        confirmText?.let(mBinding.btnConfirm::setText)
        confirmColor?.let(mBinding.btnConfirm::setTextColor)
        cancelText?.let(mBinding.btnCancel::setText)
    }

    override fun onClick(v: View) {
        super.onClick(v)
        v.clickOrRepeat() ?: return
        when (v.id) {
            R.id.btn_confirm -> {
                (onConfirm ?: defaultAction).invoke(this)
            }
            R.id.btn_cancel -> {
                (onCancel ?: defaultAction).invoke(this)
            }
        }
    }

    fun title(title: String) = apply {
        this.title = title
    }

    fun text(text: String) = apply {
        this.text = text
    }

    fun confirm(
        confirmText: String? = null,
        confirmColor: Int? = null,
        onConfirm: ((ConfirmDialog) -> Unit)? = null
    ) = apply {
        if (null != confirmText) this.confirmText = confirmText
        if (null != confirmColor) this.confirmColor = confirmColor
        if (null != onConfirm) this.onConfirm = onConfirm
    }

    fun cancel(
        cancelText: String? = null,
        onCancel: ((ConfirmDialog) -> Unit)? = null
    ) = apply {
        if (null != cancelText) this.cancelText = cancelText
        if (null != onCancel) this.onCancel = onCancel
    }

}