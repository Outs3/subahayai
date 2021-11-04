package com.outs.utils.android.view

import android.content.res.TypedArray
import android.util.DisplayMetrics
import android.util.TypedValue
import androidx.annotation.StyleableRes

/**
 * author: Outs3
 * e-mail: 3.3nosekai@gmail.com
 * date: 2021/5/15 16:25
 * desc:
 */
abstract class StringAttr : Attribute<String>() {
    override fun getFromAttrs(typedArray: TypedArray): String? =
        typedArray.getString(styleableId) ?: defValue
}

abstract class ColorAttr : Attribute<Int>() {
    override fun getFromAttrs(typedArray: TypedArray): Int =
        typedArray.getColor(styleableId, defValue ?: 0)
}

abstract class DimenAttr : Attribute<Float>() {
    override fun getFromAttrs(typedArray: TypedArray): Float =
        typedArray.getDimensionPixelSize(
            styleableId,
            defValue?.toInt() ?: 0
        )
            .toFloat()
}

abstract class DimenSpAttr
abstract class IntAttr : Attribute<Int>() {
    override fun getFromAttrs(typedArray: TypedArray): Int? =
        typedArray.getInt(styleableId, defValue ?: 0)
}

abstract class BooleanAttr : Attribute<Boolean>() {
    override fun getFromAttrs(typedArray: TypedArray): Boolean? =
        typedArray.getBoolean(styleableId, defValue ?: false)
}

fun stringAttr(
    @StyleableRes styleableId: Int,
    def: String?,
    onApplyData: (String?) -> Unit
): StringAttr =
    object : StringAttr() {
        override val styleableId: Int
            get() = styleableId

        override val defValue: String?
            get() = def

        override fun applyData() {
            onApplyData(data.value)
        }
    }

fun intAttr(@StyleableRes styleableId: Int, def: Int, onApplyData: (Int) -> Unit): IntAttr =
    object : IntAttr() {
        override val styleableId: Int
            get() = styleableId

        override val defValue: Int
            get() = def

        override fun applyData() {
            onApplyData(data.value!!)
        }
    }

fun colorAttr(
    @StyleableRes styleableId: Int,
    def: Int,
    onApplyData: (Int) -> Unit
): ColorAttr =
    object : ColorAttr() {
        override val styleableId: Int
            get() = styleableId

        override val defValue: Int
            get() = def

        override fun applyData() {
            onApplyData(data.value!!)
        }
    }

fun dimenAttr(
    @StyleableRes styleableId: Int,
    displayMetrics: DisplayMetrics,
    unit: Int,
    def: Float,
    onApplyData: (Float) -> Unit
): DimenAttr =
    object : DimenAttr() {
        override val styleableId: Int
            get() = styleableId

        override val defValue: Float
            get() = TypedValue.applyDimension(
                unit,
                def,
                displayMetrics
            )

        override fun applyData() {
            onApplyData(data.value!!)
        }
    }
