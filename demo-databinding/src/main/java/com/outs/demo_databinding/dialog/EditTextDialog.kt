package com.outs.demo_databinding.dialog

import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.outs.demo_databinding.R
import com.outs.demo_databinding.databinding.DialogEdittextBinding
import com.outs.core.android.databinding.dialog.BaseDialogFragment
import com.outs.utils.android.clickOrRepeat


/**
 * author: Outs3
 * e-mail: 3.3nosekai@gmail.com
 * date: 2021/7/20 9:07
 * desc:1
 */
class EditTextDialog(
    val save: (str: String) -> Unit,
    val title: String,
    val edithint: String,
    val isMust: Boolean,
) :
    BaseDialogFragment<DialogEdittextBinding>() {

    private val mViewModel by viewModels<EditTextModel>()

    override val canCancel: Boolean
        get() = !isMust

    override val getWidth: Int
        get() = ViewGroup.LayoutParams.MATCH_PARENT

    override val getLayoutId: Int
        get() = R.layout.dialog_edittext

    override fun initView() {
        super.initView()
        mBinding.onClick = this
        mBinding.viewModel = mViewModel
        mViewModel.isMust = this.isMust
        mBinding.title.text = title
        mBinding.editText.hint = edithint
    }

    override fun onClick(v: View) {
        super.onClick(v)
        v.clickOrRepeat() ?: return
        when (v.id) {
            R.id.ok -> {//确定
                save(mBinding.editText.text.toString())
                dismiss()
            }
            R.id.ok2 -> {//确定
                save(mBinding.editText.text.toString())
                dismiss()
            }
            R.id.cancel -> {//取消
                dismiss()
            }
        }
    }
}

