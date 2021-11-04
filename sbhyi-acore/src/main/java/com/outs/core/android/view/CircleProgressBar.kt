package com.outs.core.android.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.core.content.res.use
import com.outs.core.android.R
import kotlin.math.min

/**
 * author: Outs3
 * e-mail: 3.3nosekai@gmail.com
 * date: 2021/5/8 17:31
 * desc:
 */
class CircleProgressBar(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
    FrameLayout(context, attrs, defStyleAttr) {

    var isInit: Boolean = false
    var progress: Int = 1
        set(value) {
            field = value
            if (isInit) invalidate()
        }
    var max: Int = 10
        set(value) {
            field = value
            if (isInit) invalidate()
        }
    var progressColor = Color.BLUE
        set(value) {
            field = value
            if (isInit) invalidate()
        }
    var backgroundProgressColor = Color.TRANSPARENT
        set(value) {
            field = value
            if (isInit) invalidate()
        }
    var arcStrokeWidth = 50f
        set(value) {
            field = value
            if (isInit) invalidate()
        }

    private val progressPaint by lazy {
        Paint().apply {
            color = progressColor
            isAntiAlias = true
            style = Paint.Style.STROKE
            strokeWidth = arcStrokeWidth
        }
    }

    private val backgroundProgressPaint by lazy {
        Paint().apply {
            color = backgroundProgressColor
            isAntiAlias = true
            style = Paint.Style.STROKE
            strokeWidth = arcStrokeWidth
        }
    }

    private val center by lazy {
        width.div(2f) to height.div(2f)
    }

    private val radius by lazy {
        min(width - arcStrokeWidth, height - arcStrokeWidth).div(2f)
    }

    private val oval by lazy {
        RectF().apply {
            left = center.first.minus(radius)
            top = center.second.minus(radius)
            right = center.first.plus(radius)
            bottom = center.second.plus(radius)
        }
    }

    constructor(context: Context) : this(context, null, -1)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, -1)

    init {
        setWillNotDraw(false)
        attrs?.let { context.obtainStyledAttributes(it, R.styleable.CircleProgressBar) }
            ?.use { typedArray ->
                0.until(typedArray.indexCount).forEach {
                    when (val attr = typedArray.getIndex(it)) {
                        R.styleable.CircleProgressBar_progress -> {
                            progress = typedArray.getInt(attr, progress)
                        }
                        R.styleable.CircleProgressBar_max -> {
                            max = typedArray.getInt(attr, max)
                        }
                        R.styleable.CircleProgressBar_progressColor -> {
                            progressColor = typedArray.getColor(attr, progressColor)
                        }
                        R.styleable.CircleProgressBar_backgroundPorgressColor -> {
                            backgroundProgressColor =
                                typedArray.getColor(attr, backgroundProgressColor)
                        }
                        R.styleable.CircleProgressBar_arcStrokeWidth -> {
                            arcStrokeWidth = typedArray.getDimension(attr, arcStrokeWidth)
                        }
                    }
                }
            }
        isInit = true
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawCircle(center.first, center.second, radius, backgroundProgressPaint)
        canvas.drawArc(oval, -90f, progress.toFloat().div(max).times(360), false, progressPaint)
    }

}