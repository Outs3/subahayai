package com.outs.core.android.databinding.dialog

import android.view.Gravity
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.LinearLayoutCompat
import com.blankj.utilcode.util.SizeUtils
import com.outs.core.android.databinding.R
import com.outs.core.android.databinding.databinding.DialogBottomSelectBinding
import com.outs.utils.android.attrColor

/**
 * author: Outs3
 * e-mail: 3.3nosekai@gmail.com
 * date: 2021/6/13 16:36
 * desc:
 */
class BottomSelectDialog : BaseDialogFragment<DialogBottomSelectBinding>() {

    private var onConfirm: OnConfirmListener? = null
    private var onCancel: OnCancelListener = object : OnCancelListener {}
    private var onItemCreated: (AppCompatTextView) -> Unit = {}
    private val contents = ArrayList<String>()

    override val getLayoutId: Int
        get() = R.layout.dialog_bottom_select

    override val getWidth: Int
        get() = ViewGroup.LayoutParams.MATCH_PARENT

    override val getGravity: Int
        get() = Gravity.BOTTOM

    override fun initView() {
        super.initView()
        mBinding.textCancel.setOnClickListener {
            onCancel.onCancel(this)
        }
        val context = requireContext()
        val colorPrimary = context.attrColor(androidx.appcompat.R.attr.colorPrimary)
        contents
            .mapIndexed { index, s ->
                AppCompatTextView(context).apply {
                    gravity = Gravity.CENTER
                    text = s
                    textSize = 16f
                    setTextColor(colorPrimary)
                    setOnClickListener { onConfirm?.onConfirm(this@BottomSelectDialog, index, s) }
                }
                    .also(onItemCreated)
            }
            .forEach {
                mBinding.layoutContent.addView(it)
                it.layoutParams = it.layoutParams?.apply {
                    if (this is LinearLayoutCompat.LayoutParams) {
                        this.height = SizeUtils.dp2px(50f)
                    }
                }
            }
    }

    fun content(s: List<String>) = this.apply { contents.addAll(s) }

    fun content(vararg s: String): BottomSelectDialog = this.apply { contents.addAll(s) }

    fun onItemCreated(onItemCreated: (AppCompatTextView) -> Unit) =
        this.also { it.onItemCreated = onItemCreated }

    fun onCancel(onCancel: OnCancelListener): BottomSelectDialog {
        this.onCancel = onCancel
        return this
    }

    fun onConfirm(onConfirm: OnConfirmListener): BottomSelectDialog {
        this.onConfirm = onConfirm
        return this
    }

    fun onConfirm(onConfirm: (dialog: BottomSelectDialog, position: Int, text: String) -> Unit): BottomSelectDialog {
        this.onConfirm = object : OnConfirmListener {
            override fun onConfirm(dialog: BottomSelectDialog, position: Int, text: String) {
                onConfirm(dialog, position, text)
            }
        }
        return this
    }

    fun callback(cb: Callback): BottomSelectDialog {
        onConfirm = cb
        onCancel = cb
        return this
    }

    interface OnCancelListener {
        fun onCancel(dialog: BottomSelectDialog) {
            dialog.dismiss()
        }
    }

    interface OnConfirmListener {
        fun onConfirm(dialog: BottomSelectDialog, position: Int, text: String)
    }

    interface Callback : OnCancelListener, OnConfirmListener
}