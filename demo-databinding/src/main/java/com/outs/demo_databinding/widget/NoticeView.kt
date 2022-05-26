package com.outs.demo_databinding.widget

import android.content.Context
import android.graphics.Typeface
import android.text.TextUtils
import android.util.AttributeSet
import android.view.Gravity
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.TextView
import android.widget.ViewFlipper
import androidx.core.content.ContextCompat
import com.outs.demo_databinding.R
import com.outs.utils.android.dp2px

/**
 * author: Outs3
 * e-mail: 3.3nosekai@gmail.com
 * date: 2021/8/1 17:16
 * desc:
 */
class NoticeView(context: Context, attrs: AttributeSet?) : ViewFlipper(context, attrs) {

    constructor(context: Context) : this(context, null)

    init {
        val dp5 = dp2px(5)
        //自动开始
        isAutoStart = true
        // 轮播间隔时间为3s
        flipInterval = 3000
        // 内边距5dp
        setPadding(dp5, dp5, dp5, dp5)
        // 设置enter和leave动画
        inAnimation = AnimationUtils.loadAnimation(context, R.anim.notice_in)
        outAnimation = AnimationUtils.loadAnimation(context, R.anim.notice_out)
    }

    fun add(
        notice: String,
        bold: Boolean? = null,
        color: Int? = null,
        onClick: OnClickListener? = null
    ) {
        TextView(context)
            .apply {
                isSingleLine = true
                text = notice
                textSize = 14f
                if (true == bold) setTypeface(Typeface.DEFAULT, Typeface.BOLD)
//                paint.strokeWidth = strokeWidth?.toFloat() ?: 10f
                ellipsize = TextUtils.TruncateAt.END
                gravity = Gravity.CENTER_VERTICAL
                setTextColor(color ?: ContextCompat.getColor(context, com.outs.core.android.R.color.black_333))
                onClick?.let(::setOnClickListener)
                layoutParams = LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            }
            .also(::addView)
    }

    fun <T> reset(data: List<T>, display: (T) -> String, onClick: OnClickListener? = null) {
        clear()
        data.forEach { item ->
            add(display(item), onClick = onClick)
        }
    }

    fun clear() {
        removeAllViews()
    }

}