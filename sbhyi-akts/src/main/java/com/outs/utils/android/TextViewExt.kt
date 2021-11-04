package com.outs.utils.android

import android.text.SpannableString
import android.text.Spanned
import android.text.style.LeadingMarginSpan
import android.widget.TextView

/**
 * author: Outs3
 * e-mail: 3.3nosekai@gmail.com
 * date: 2021/5/15 16:25
 * desc:
 */

/**
 * 首行缩进文本
 *      length   要缩进的宽度  px单位
 *      desc   要展示的文本
 */
fun getSpannableString(marginSpanSize: Int, desc: String): SpannableString {
    val spannableString = SpannableString(desc)
    spannableString.setSpan(
        LeadingMarginSpan.Standard(marginSpanSize, 0),  //仅首行缩进
        0,
        desc.length,
        Spanned.SPAN_INCLUSIVE_EXCLUSIVE
    )
    return spannableString
}

/**
 * 缩进
 *      marginLeftFirst： 首行缩进，px单位
 *      marginLeftRest: 其余行缩进，px单位
 */
fun SpannableString.marginLeft(marginLeftFirst: Int, marginLeftRest: Int = 0) {
    setSpan(
        LeadingMarginSpan.Standard(marginLeftFirst, marginLeftRest),
        0,
        length,
        Spanned.SPAN_INCLUSIVE_EXCLUSIVE
    )
}

fun TextView.intText(def: Int = 0): Int = try {
    this.text.toString().toInt()
} catch (e: Exception) {
    def
}