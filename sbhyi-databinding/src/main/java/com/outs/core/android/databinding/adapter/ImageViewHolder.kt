package com.outs.core.android.databinding.adapter

import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView

/**
 * author: Outs3
 * e-mail: 3.3nosekai@gmail.com
 * date: 2021/8/10 14:20
 * desc:
 */
class ImageViewHolder(
    val image: AppCompatImageView = AppCompatImageView(com.outs.utils.android.appInstance).also {
        it.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
    }
) : RecyclerView.ViewHolder(image) {
    var selfPosition: Int = -1
}