package com.outs.demo_databinding.dialog

import android.content.Context
import android.view.Gravity
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import com.bigkoo.pickerview.builder.OptionsPickerBuilder
import com.bigkoo.pickerview.view.OptionsPickerView
import com.outs.demo_databinding.R
import com.outs.utils.android.onClickOrRepeat
import com.outs.utils.android.postOnNot
import com.outs.utils.kotlin.tryAsObj

/**
 * author: Outs3
 * e-mail: 3.3nosekai@gmail.com
 * date: 2021/10/3 11:59
 * desc:
 */
private data class Province(val name: String, val value: List<City>)

private data class City(val name: String, val value: List<String>)

open class CityPicker(context: Context, onCitySelect: (String) -> Unit) {

    private val src: List<Province> by lazy {
        context.assets.open("province.json")
            .readBytes()
            .let { String(it) }
            .tryAsObj<List<Province>>()
            ?: emptyList()
    }

    val view: OptionsPickerView<String> by lazy {
        OptionsPickerBuilder(context) { position1, position2, position3, v ->
            onCitySelect(getAddress(position1, position2, position3))
        }
            .setLayoutRes(R.layout.view_opt_picker) { v ->
                v?.findViewById<Button>(R.id.btnCancel)?.onClickOrRepeat { view.dismiss() }
                v?.findViewById<Button>(R.id.btnSubmit)?.onClickOrRepeat {
                    view.apply {
                        returnData()
                        dismiss()
                    }
                }
            }
            .setTextColorCenter(ContextCompat.getColor(context, R.color.lime_17a953))
            .setTextColorOut(ContextCompat.getColor(context, R.color.black_999))
            .setItemVisibleCount(5)
            .setContentTextSize(16)
            .isDialog(true)
            .build<String>()
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
            .also {
                it.setPicker(
                    ArrayList(src.map { it.name }),
                    ArrayList(src.map { it.value.map { it.name } }),
                    ArrayList(src.map { it.value.map { it.value } })
                )
            }
    }

    fun show(addr: String? = null) {
        getPositions(addr).let {
            view.setSelectOptions(it[0], it[1], it[2])
        }
        view.show()
    }

    private fun getAddress(position1: Int, position2: Int, position3: Int): String {
        val province = src.getOrNull(position1)
        val city = province?.value?.getOrNull(position2)
        val area = city?.value?.getOrNull(position3)
        return "${province?.name ?: ""} - ${city?.name ?: ""} - ${area ?: ""}"
    }

    private fun indexOfProvince(name: String): Int? =
        src.indexOfFirst { name == it.name }
            .takeIf { -1 != it }

    private fun Province?.indexOfCity(name: String): Int? =
        this?.value?.indexOfFirst { name == it.name }
            ?.takeIf { -1 != it }

    private fun City?.indexOfArea(name: String): Int? =
        this?.value?.indexOfFirst { name == it }
            ?.takeIf { -1 != it }

    private fun getPositions(address: String? = null): IntArray {
        val names = address?.split("-")?.map { it.trim() }
        val provinceName = names?.getOrNull(0)
        val provincePosition = provinceName?.let(::indexOfProvince)
        val province = provincePosition?.let(src::getOrNull)
        val cityName = names?.getOrNull(1)
        val cityPosition = cityName?.let { province.indexOfCity(it) }
        val city = cityPosition?.let { province?.value?.getOrNull(it) }
        val areaName = names?.getOrNull(2)
        val areaPosition = areaName?.let { name -> city?.indexOfArea(name) }
        return intArrayOf(provincePosition ?: 0, cityPosition ?: 0, areaPosition ?: 0)
    }

}

open class LiveCityPicker(context: Context, val target: MutableLiveData<String>) :
    CityPicker(context, target::postOnNot) {
    fun show() = super.show(target.value)
}