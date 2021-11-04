package com.outs.demo_databinding.dialog

import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import com.outs.demo_databinding.R
import com.outs.demo_databinding.databinding.DialogShareBinding
import com.outs.core.android.databinding.dialog.BaseDialogFragment
import com.outs.utils.android.clickOrRepeat
import com.outs.utils.android.intent.Extra
import com.outs.utils.kotlin.toDoPage

/**
 * author: Outs3
 * e-mail: 3.3nosekai@gmail.com
 * date: 2021/8/23 16:47
 * desc:
 */
class ShareDialog : BaseDialogFragment<DialogShareBinding>() {

    @Extra
    private var title: String = ""

    @Extra
    private var summary: String = ""

    @Extra
    private var iconUrl: String = ""

    @Extra
    private var linkUrl: String = ""

    override val getWidth: Int
        get() = ViewGroup.LayoutParams.MATCH_PARENT

    override val getGravity: Int
        get() = Gravity.BOTTOM

    override val getLayoutId: Int
        get() = R.layout.dialog_share

    override fun initView() {
        super.initView()
        mBinding.onClick = this
    }

    override fun onClick(v: View) {
        super.onClick(v)
        v.clickOrRepeat() ?: return
        when (v.id) {
            R.id.btn_close -> dismiss()
            R.id.layer_wechat -> {
                "分享到微信".toDoPage()
//                WXUtil.shareToWechat(title, summary, linkUrl)
            }
            R.id.layer_moments -> {
                "分享到朋友圈".toDoPage()
//                WXUtil.shareToWechatMoments(title, summary, linkUrl)
            }
        }
    }

}