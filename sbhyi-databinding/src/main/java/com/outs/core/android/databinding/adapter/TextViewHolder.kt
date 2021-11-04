package com.outs.core.android.databinding.adapter

import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.outs.utils.android.appInstance

/**
 * author: Outs3
 * e-mail: 3.3nosekai@gmail.com
 * date: 2021/8/10 14:20
 * desc:
 */
class TextViewHolder(val text: AppCompatTextView = AppCompatTextView(com.outs.utils.android.appInstance)) :
    RecyclerView.ViewHolder(text)