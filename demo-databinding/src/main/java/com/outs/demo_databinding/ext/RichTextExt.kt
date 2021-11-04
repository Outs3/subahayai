package com.outs.demo_databinding.ext

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.zzhoujay.richtext.RichText

/**
 * author: Outs3
 * e-mail: 3.3nosekai@gmail.com
 * date: 2021/8/11 18:19
 * desc:
 */
@BindingAdapter("android:richText")
fun TextView.richText(text: String? = null) = RichText.from(text ?: "").into(this)