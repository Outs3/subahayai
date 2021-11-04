package com.outs.demo_databinding.widget

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.outs.demo_databinding.R
import com.outs.demo_databinding.databinding.ItemIndicatorCircleBinding
import com.outs.core.android.databinding.data.adapter.SingleChoiceDataAdapter
import com.outs.core.android.databinding.holder.CheckedAutoItem
import com.outs.core.android.databinding.holder.DataBindingViewHolder

/**
 * author: Outs3
 * e-mail: 3.3nosekai@gmail.com
 * date: 2021/8/21 16:03
 * desc:
 */
abstract class IndicatorView<VH : DataBindingViewHolder<Int, *>>(
    context: Context,
    attrs: AttributeSet?,
    defStyleAttr: Int
) : RecyclerView(context, attrs, defStyleAttr) {

    val indicatorAdapter: SingleChoiceDataAdapter<Int> by lazy {
        SingleChoiceDataAdapter(
            context,
            onCheckChanged = { model, position ->
                if (-1 != position) {
                    onPageChanged?.invoke(position)
                }
            },
            createHolder = { createHolder(it) }
        )
    }

    var onPageChanged: ((Int) -> Unit)? = null

    abstract fun createHolder(itemType: Int): VH

    constructor(context: Context) : this(context, null, -1)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, -1)

    init {
        indicatorAdapter.bind(
            recyclerView = this,
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        )
    }

    fun setPageCount(count: Int) {
        indicatorAdapter.data.reset((0 until count).toList())
    }

    fun setPageNum(pageNum: Int) {
        if (indicatorAdapter.checked != pageNum) {
            indicatorAdapter.updateChecked(pageNum)
        }
    }

}

private class AbsIndicatorView<VH : DataBindingViewHolder<Int, *>>(
    context: Context,
    val createItemHolder: (Int) -> VH
) : IndicatorView<VH>(context) {
    override fun createHolder(itemType: Int): VH = createItemHolder(itemType)
}

private class IndicatorCircleItem(val checkedColor: Int, val uncheckedColor: Int) :
    CheckedAutoItem<Int, ItemIndicatorCircleBinding>(R.layout.item_indicator_circle) {
    override fun setViews() {
        super.setViews()
        mBinding.checkedColor = checkedColor
        mBinding.uncheckedColor = uncheckedColor
    }
}

private class CircleIndicatorView(
    context: Context,
    val checkedColor: Int = Color.BLACK,
    val uncheckedColor: Int = Color.WHITE
) :
    IndicatorView<IndicatorCircleItem>(context) {
    override fun createHolder(itemType: Int): IndicatorCircleItem =
        IndicatorCircleItem(checkedColor, uncheckedColor)
}

fun <VH : DataBindingViewHolder<Int, *>> absIndicator(
    context: Context,
    createHolder: (Int) -> VH
): IndicatorView<VH> = AbsIndicatorView(context, createHolder)

fun circleIndicator(
    context: Context,
    checkedColor: Int = Color.BLACK,
    uncheckedColor: Int = Color.WHITE
): IndicatorView<*> = CircleIndicatorView(context, checkedColor, uncheckedColor)