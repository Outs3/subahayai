package com.outs.demo_databinding.widget

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.MotionEvent
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.outs.demo_databinding.R
import com.outs.core.android.databinding.adapter.BaseViewHolder
import com.outs.core.android.databinding.adapter.CommonAdapter
import com.outs.core.android.databinding.data.adapter.SingleChoiceDataAdapter
import com.outs.core.android.databinding.holder.DataBindingViewHolder
import com.outs.demo_databinding.widget.IndicatorView
import com.outs.utils.android.removeParent
import com.outs.utils.kotlin.reset
import com.outs.utils.kotlin.typeOfOrNull

/**
 * author: Outs3
 * e-mail: 3.3nosekai@gmail.com
 * date: 2021/8/11 9:00
 * desc:
 */
abstract class RecyclerViewPager<T : Any, VDB : ViewDataBinding, VH : DataBindingViewHolder<T, VDB>>(
    private val mContext: Context,
    attrs: AttributeSet?,
    defStyleAttr: Int
) :
    LinearLayoutCompat(mContext, attrs, defStyleAttr) {

    //页数
    val pageNum by lazy { MutableLiveData<Int>() }

//    var useIndicator: Boolean = true
//        set(value) {
//            field = value
//            indicatorRecycler.visibleOrNot(value)
//        }

    //列数
    var spanCount: Int = 4
        set(value) {
            field = value
            regroupData()
        }

    //行数
    var lineCount: Int = 2
        set(value) {
            field = value
            regroupData()
        }

    val checked: T? get() = mChecked

    var onCheckedChanged: ((T?) -> Unit)? = null

    //方向
    @ViewPager2.Orientation
    var pageOrientation: Int = ViewPager2.ORIENTATION_HORIZONTAL
        set(value) {
            field = value
            viewPager.orientation = value
        }

    private var indicator: IndicatorView<*>? = null

    //一页显示的数据量
    private val pageLimit: Int get() = spanCount * lineCount

    private val pageAdapter by lazy { CommonAdapter.simpleAdapter { PageItem() } }

    private val data by lazy { ArrayList<T>() }

    private var mChecked: T? = null

    private val viewPager: ViewPager2 = ViewPager2(mContext)
        .apply {
            orientation = pageOrientation
            layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        }
        .also(::addView)

//    private val indicatorRecycler: RecyclerView = RecyclerView(mContext)
//        .apply {
//            orientation = RecyclerView.HORIZONTAL
//            setPadding(dp2px(10))
//            layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, dp2px(35))
//        }
//        .also(::addView)

    constructor(context: Context) : this(context, null, -1)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, -1)

    init {
        orientation = VERTICAL
        viewPager.adapter = pageAdapter
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                onPageChanged(position)
            }
        })
    }

    open fun setIndicator(indicator: IndicatorView<*>) {
        this.indicator?.removeParent()
        indicator.onPageChanged = { if (it != pageNum.value) setPageNum(it) }
        addView(indicator, LayoutParams(
            LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT
        ).also {
            it.gravity = Gravity.CENTER_HORIZONTAL
        })
        this.indicator = indicator
    }

    open fun setPageNum(position: Int) {
        viewPager.setCurrentItem(position, true)
    }

    open fun setData(data: List<T>) {
        this.data.reset(data)
        regroupData()
    }

    open fun regroupData() {
        val total = data.size
        val pageCount = when {
            0 == total -> 0
            pageLimit >= total -> 1
            0 == total % pageLimit -> total / pageLimit
            else -> total / pageLimit + 1
        }
        val groupedData = if (0 == pageCount) {
            arrayListOf()
        } else {
            val pageList = ArrayList<Page<T>>(pageCount)
            var pageIndex = 0
            var currentPage: Page<T> = Page(pageIndex++, ArrayList())
            pageList.add(currentPage)
            for ((index, gift) in data.withIndex()) {
                if (0 != index && 0 == index % pageLimit) {
                    currentPage = Page(pageIndex++, ArrayList())
                    pageList.add(currentPage)
                }
                currentPage.data.add(gift)
            }
            pageList
        }
        applyData(groupedData)
    }

    open fun applyData(data: ArrayList<Page<T>>) {
        pageAdapter.data.clear()
        pageAdapter.addList(data)
        indicator?.setPageCount(data.size)
    }

    protected open fun onChangedChanged(newChecked: T?) {
        mChecked = newChecked
        onCheckedChanged?.invoke(newChecked)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        when (ev?.action) {
            MotionEvent.ACTION_DOWN -> {
                parent.requestDisallowInterceptTouchEvent(true)
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL ->
                parent.requestDisallowInterceptTouchEvent(false)
        }
        return super.onInterceptTouchEvent(ev)
    }

    abstract fun newViewHolder(): VH

    open fun onPageChanged(pageNum: Int) {
        this.pageNum.value = pageNum
        indicator?.setPageNum(pageNum)
    }

    private inner class PageItem : BaseViewHolder<Page<T>>() {

        private val itemAdapter by lazy { ItemAdapter() }

        override val getLayoutId: Int
            get() = R.layout.item_recycler_view

        override fun setViews() {
            super.setViews()
            rootView.typeOfOrNull<RecyclerView>()?.let {
                it.layoutManager =
                    GridLayoutManager(context, spanCount, GridLayoutManager.VERTICAL, false)
                it.adapter = itemAdapter
            }
        }

        override fun onAttach() {
            super.onAttach()
            itemAdapter.updateChecked(null)
        }

        override fun onDetach() {
            super.onDetach()
            itemAdapter.notifyDataSetChanged()
        }

        override fun handleData(model: Page<T>?, position: Int) {
            if (null != model) {
                onPageChanged(model.pageNo)
                itemAdapter.data.reset(model.data)
            }
        }

    }

    private inner class ItemAdapter : SingleChoiceDataAdapter<T>(
        mContext,
        onCheckChanged = { model, position -> onChangedChanged(model) },
        createHolder = { newViewHolder() }
    )

    class Page<T>(
        val pageNo: Int,
        val data: MutableList<T>
    )

}