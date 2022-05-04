package com.outs.core.android.view

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.widget.FrameLayout

/**
 * author: Outs3
 * e-mail: 3.3nosekai@gmail.com
 * date: 2022/1/5 14:13
 * desc: 这个控件的布局时（onLayout）会根据自身的实际展示宽度高度来布局 方便子控件基于屏幕展示居中而不是基于实际宽高
 */
class DynamicVisibleLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val mLocalVisibleRect = Rect()

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        mLocalVisibleRect
            .also(::getLocalVisibleRect)
            .also { rect ->
                super.onLayout(
                    changed,
                    rect.left,
                    rect.top,
                    rect.right,
                    rect.bottom
                )
            }
    }

}