package com.outs.demo_databinding.base.adapter

import android.annotation.SuppressLint
import android.view.ViewGroup
import android.widget.ImageView
import com.outs.core.android.databinding.adapter.ImageViewHolder
import com.outs.core.android.databinding.holder.OnModelClickListener
import com.outs.utils.android.onClickOrRepeat
import com.outs.utils.kotlin.reset
import com.youth.banner.adapter.BannerAdapter

/**
 * author: Outs3
 * e-mail: 3.3nosekai@gmail.com
 * date: 2021/8/10 14:23
 * desc:
 */
open class SimpleBannerAdapter<T : Any>(
    private val src: MutableList<T> = mutableListOf(),
    private val load: (ImageView, T?) -> Unit
) :
    BannerAdapter<T, ImageViewHolder>(src) {

    var onModelClick: OnModelClickListener<T>? = null

    override fun onCreateHolder(
        parent: ViewGroup?,
        viewType: Int
    ): ImageViewHolder = ImageViewHolder().apply {
        image.onClickOrRepeat {
            val position = absoluteAdapterPosition
            val model = src.getOrNull(position)
            if (null != model) {
                onModelClick?.onModelClick(position, model)
            }
        }
    }

    override fun onBindView(
        holder: ImageViewHolder?,
        data: T?,
        position: Int,
        size: Int
    ) {
        val view = holder?.image ?: return
        load(view, data)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun reset(data: List<T>) {
        src.reset(data)
        notifyDataSetChanged()
    }

}