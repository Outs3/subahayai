package com.outs.demo_databinding.dialog

import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import androidx.lifecycle.MutableLiveData
import com.bigkoo.pickerview.builder.TimePickerBuilder
import com.bigkoo.pickerview.view.TimePickerView
import com.outs.demo_databinding.R
import com.outs.utils.android.onClickOrRepeat
import com.outs.utils.android.postOnNot
import com.outs.utils.kotlin.format
import com.outs.utils.kotlin.parseDate
import java.util.*

/**
 * author: Outs3
 * e-mail: 3.3nosekai@gmail.com
 * date: 2021/10/2 9:59
 * desc:
 */
open class BaseDatePicker(
    context: Context,
    type: BooleanArray = booleanArrayOf(
        true,
        true,
        true,
        false,
        false,
        false
    ),
    onTimeSelect: (Long) -> Unit
) {
    val view: TimePickerView by lazy {
        TimePickerBuilder(context) { date: Date?, v: View? -> date?.time?.let(onTimeSelect) }
            .setLayoutRes(
                R.layout.view_time_picker
            ) { v ->
                v?.findViewById<Button>(R.id.btnCancel)?.onClickOrRepeat { view.dismiss() }
                v?.findViewById<Button>(R.id.btnSubmit)?.onClickOrRepeat {
                    view.apply {
                        returnData()
                        dismiss()
                    }
                }
            }
            .setType(type)
            .setContentTextSize(16)
            .isDialog(true)
            .build()
            .also {
                it.dialogContainerLayout?.layoutParams = FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    Gravity.BOTTOM
                )
                it.dialog?.window?.apply {
                    setGravity(Gravity.BOTTOM)
                    setDimAmount(0.7f)
                }
            }
    }

    fun show(date: Calendar? = null) {
        date?.let(view::setDate)
        view.show()
    }

}

open class DatePicker(
    context: Context,
    private val pattern: String,
    type: BooleanArray = booleanArrayOf(true, true, true, false, false, false),
    onTimeSelect: (String) -> Unit
) : BaseDatePicker(context, type, { it.format(pattern).let(onTimeSelect) }) {
    fun show(date: String?) =
        show(Calendar.getInstance().also { it.timeInMillis = date?.parseDate(pattern) ?: 0 })
}

open class LiveDatePicker(
    context: Context,
    pattern: String,
    type: BooleanArray = booleanArrayOf(true, true, true, false, false, false),
    val target: MutableLiveData<String>
) : DatePicker(
    context, pattern, type, target::postOnNot
) {
    fun show() = super.show(target.value)
}

