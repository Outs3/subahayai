package com.outs.utils.android

import android.content.Context
import android.view.*
import android.widget.CompoundButton
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import com.outs.utils.kotlin.getFieldValue
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.math.absoluteValue

/**
 * author: Outs3
 * e-mail: 3.3nosekai@gmail.com
 * date: 2021/4/7 15:41
 * desc:
 */
fun View.visible() {
    visibility = View.VISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun Int.inflate(context: Context, parent: ViewGroup? = null): View =
    LayoutInflater.from(context).inflate(this, parent)

fun View.parents(): Flow<ViewParent> = flow {
    var parent = parent
    while (null != parent) {
        emit(parent)
        parent = parent.parent
    }
}

fun View.removeParent() {
    parent?.let { if (it is ViewGroup) it.removeView(this) }
}

fun <T : View> T.onSlideRight(onSlideRight: () -> Unit) {
    oneceDownUpTouch { down, up ->
        if (isGestureSlideRight(down, up)) onSlideRight()
    }
}

fun <T : View> T.oneceDownUpTouch(onDownUp: (down: MotionEvent, up: MotionEvent) -> Unit) {
    var mLastDown: MotionEvent? = null
    var mLastUp: MotionEvent?
    this.setOnTouchListener { v, event ->
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                mLastDown = MotionEvent.obtain(event)
            }
            MotionEvent.ACTION_UP -> {
                mLastUp = MotionEvent.obtain(event)
                if (null != mLastDown && null != mLastUp) {
                    onDownUp(mLastDown!!, mLastUp!!)
                }
            }
        }
        false
    }
}

fun View.onClickPlus(
    duration: Long = 300,
    onClick: View.OnClickListener? = null,
    onLongClick: View.OnClickListener? = null,
    onLongClickUp: (() -> Unit)? = null
) {
    var isLongClick = false
    if (null != onLongClick) {
        setOnLongClickListener {
            onLongClick.onClick(it)
            false
        }
    }
    val detector = GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
        override fun onDown(e: MotionEvent?): Boolean {
            isLongClick = false
            return true
        }

        override fun onLongPress(e: MotionEvent?) {
            isLongClick = true
        }

    })
    setOnTouchListener { v, event ->
        detector.onTouchEvent(event)
        when (event.action) {
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                if (isLongClick) {
                    onLongClickUp?.invoke()
                } else if (null != onClick) {
                    v.clickOrRepeat(duration)?.let(onClick::onClick)
                    v.performClick()
                }
            }
        }
        false
    }
}

/***
 * 是否右滑手势
 */
fun isGestureSlideRight(down: MotionEvent, up: MotionEvent): Boolean {
    val distanceX = up.x - down.x
    val distanceY = up.y - down.y
    return distanceY.absoluteValue <= 75 && distanceX >= 400
}

inline fun <reified P : ViewGroup.LayoutParams> View.updateLayoutParams(block: P.() -> P) {
    val params = layoutParams
    if (params is P) {
        layoutParams = params.block()
    }
}

fun RecyclerView.addDividerItemDecoration(
    context: Context,
    orientation: Int,
    drawableId: Int
) {
    addItemDecoration(drawableId.asDividerItemDecoration(context, orientation))
}

fun Int.asDividerItemDecoration(
    context: Context,
    orientation: Int = DividerItemDecoration.VERTICAL
): DividerItemDecoration = DividerItemDecoration(context, orientation).also {
    ContextCompat.getDrawable(context, this)?.let(it::setDrawable)
}

//The key must be an application-specific resource id.
private val tagLastClick = View.generateViewId()

fun View.clickOrRepeat(duration: Long = 300): View? {
    val cur = System.currentTimeMillis()
    val lastClick = getTag(tagLastClick).let { if (it is Long) it else 0L }
    val isRepeat = cur - lastClick < duration
    return if (isRepeat) null else this.also {
        setTag(tagLastClick, cur)
    }
}

fun View.onClickOrRepeat(duration: Long = 300, onClickListener: View.OnClickListener) {
    this.setOnClickListener { it.clickOrRepeat(duration)?.let(onClickListener::onClick) }
}

fun <T : Any> TabLayout.reset(data: List<T>, getText: (T) -> String = Any::toString) {
    removeAllTabs()
    data.mapIndexed { index, s ->
        addTab(newTab().apply {
            text = getText(s)
            tag = s
        }, 0 == index)
    }
}

class TabCheckedListener(val onChecked: (TabLayout.Tab) -> Unit) :
    TabLayout.OnTabSelectedListener {

    override fun onTabSelected(tab: TabLayout.Tab?) {
        tab?.let(onChecked)
    }

    override fun onTabUnselected(tab: TabLayout.Tab?) {
    }

    override fun onTabReselected(tab: TabLayout.Tab?) {
        tab?.let(onChecked)
    }

}

fun RecyclerView.addOnScrollIdle(onScrollIdle: (RecyclerView) -> Unit): RecyclerView.OnScrollListener =
    object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (RecyclerView.SCROLL_STATE_IDLE == newState) onScrollIdle(recyclerView)
        }
    }.also(::addOnScrollListener)


fun CompoundButton.checkWithoutCB(checked: Boolean): Boolean = try {
    if (isChecked != checked) {
        val listener = getFieldValue<CompoundButton, Any>("mOnCheckedChangeListener", true)
        setOnCheckedChangeListener(null)
        isChecked = checked
        setOnCheckedChangeListener(listener as CompoundButton.OnCheckedChangeListener?)
    }
    true
} catch (e: Exception) {
    e.printStackTrace()
    false
}
