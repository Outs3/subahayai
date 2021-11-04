package com.outs.core.android.databinding

import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.ColorStateListDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.text.Html
import android.view.KeyEvent
import android.view.View
import android.webkit.WebView
import android.widget.*
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.outs.core.android.databinding.data.adapter.SimpleDataAdapter
import com.outs.core.android.databinding.data.adapter.SingleChoiceDataAdapter
import com.outs.core.android.databinding.data.source.DataSource
import com.outs.core.android.databinding.holder.OnModelClickListener
import com.outs.utils.android.glide.*
import com.outs.utils.kotlin.emptyToNull
import com.outs.utils.kotlin.format
import com.outs.utils.kotlin.typeOfOrNull
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import java.text.SimpleDateFormat
import java.util.*
import kotlin.random.Random

/**
 * author: Outs3
 * e-mail: 3.3nosekai@gmail.com
 * date: 2021/4/11 22:55
 * desc:
 */
@BindingAdapter(
    value = [
        "android:singleChoiceData",
        "android:itemLayoutId",
        "android:onCheckedChange",
        "android:defaultChecked",
    ],
    requireAll = false
)
fun RecyclerView.singleChoiceData(
    data: List<Any>?,
    @LayoutRes itemLayoutId: Int,
    onCheckChanged: ((Any?, Int) -> Unit)? = null,
    defaultChecked: Any? = null
) {
//    onCheckChanged: ((T?, Int) -> Unit)? = null,
//    checked: T? = null,
    if (null != data)
        SingleChoiceDataAdapter(
            context,
            data = data,
            onCheckChanged = onCheckChanged,
            checked = defaultChecked,
            itemLayoutId = itemLayoutId
        )
            .bind(
                context.typeOfOrNull<LifecycleOwner>(),
                this,
                refreshOnCreate = true
            )
}

@BindingAdapter(
    value = ["android:simpleData", "android:itemLayoutId"],
    requireAll = true
)
fun <T : Any> RecyclerView.simpleData(
    data: List<T>? = null,
    @LayoutRes itemLayoutId: Int,
) {
    SimpleDataAdapter(context, data ?: emptyList(), itemLayoutId)
        .bind(
            context.typeOfOrNull<LifecycleOwner>(),
            this,
            refreshOnCreate = true
        )
}

@BindingAdapter(
    value = ["android:dataSource", "android:itemLayoutId", "android:onModelClick"],
    requireAll = true
)
fun <T : Any> RecyclerView.dataSource(
    data: DataSource<T>,
    @LayoutRes itemLayoutId: Int,
    onModelClick: OnModelClickListener<T>? = null
) {
    SimpleDataAdapter(context, data, itemLayoutId, onModelClick).bind(
        context.typeOfOrNull<LifecycleOwner>(),
        this,
        refreshOnCreate = true
    )
}

private val tagUrl = Random.nextInt()

@BindingAdapter(value = ["android:uri", "android:src", "android:radiu"], requireAll = false)
fun ImageView.uri(uri: Uri? = null, @DrawableRes src: Int = -1, radiu: Int = 0) {
    when {
        null != uri -> {
            val origin = getTag(tagUrl)
            if (origin != uri) {
                setTag(tagUrl, uri)
                setImageURI(uri)
            }
        }
        -1 != src -> {
            setImageResource(src)
        }
    }
}

@BindingAdapter("android:clickTag")
fun View.tag(tag: Any?) {
    this.tag = tag
}

@BindingAdapter("android:onCheckChangeListener")
fun RadioGroup.onCheckChanged(onCheckedChangeListener: RadioGroup.OnCheckedChangeListener) {
    setOnCheckedChangeListener(onCheckedChangeListener)
}

@BindingAdapter("android:onPageChangeListener")
fun ViewPager.onPageChanged(onPageChangeListener: ViewPager.OnPageChangeListener) {
    addOnPageChangeListener(onPageChangeListener)
}

@BindingAdapter("android:onFocusChangeListener")
fun EditText.onFocusChanged(onFocusChangeListener: View.OnFocusChangeListener) {
    this.onFocusChangeListener = onFocusChangeListener
}

@BindingAdapter("android:percentage")
fun TextView.percentage(value: Double) {
    text = value.times(100).toInt().let { "$it%" }
}

@BindingAdapter("android:textAny")
fun TextView.textAny(value: Any?) {
    text = value.toString()
}

@BindingAdapter("android:textHtml")
fun TextView.textHtml(value: String?) {
    if (!value.isNullOrEmpty())
        text = Html.fromHtml(value)
}

@BindingAdapter("android:visibleOrNot")
fun View.visibleOrNot(visible: Boolean) {
    visibility = if (visible) View.VISIBLE else View.GONE
}

@BindingAdapter("android:visibleOrIn")
fun View.visibleOrIn(visible: Boolean) {
    visibility = if (visible) View.VISIBLE else View.INVISIBLE
}

@BindingAdapter("android:imageBmp")
fun ImageView.imageBmp(bitmap: Bitmap?) {
    setImageBitmap(bitmap)
}

@BindingAdapter("android:fitCenter")
fun ImageView.fit(image: String?) {
    val tag = getTag(tagGlide)
    if (tag == null || tag != image)
        fitCenter(image)
}

@BindingAdapter("android:image")
fun ImageView.image(image: String?) {
    val tag = getTag(tagGlide)
    if (tag == null || tag != image)
        loadUrl(image)
}

@BindingAdapter("android:imageOnNotNull")
fun ImageView.imageOnNotNull(image: String?) {
    val tag = getTag(tagGlide)
    if (null != image?.emptyToNull() && (tag == null || tag != image))
        loadUrl(image)
}

@BindingAdapter(value = ["android:imageOnSuccess", "android:imageOnError"], requireAll = false)
fun ImageView.imageOnSuccess(image: String?, imageOnError: Drawable? = null) {
    val tag = getTag(tagGlide)
    if (tag == null || tag != image)
        loadUrl(image, imageOnError)
}

@BindingAdapter("android:imageR")
fun ImageView.imageR(image: Int) {
    setImageResource(image)
}

@BindingAdapter("android:imageNoCache")
fun ImageView.imageNoCache(image: String?) {
    val tag = getTag(tagGlide)
    if (tag == null || tag != image)
        loadUrlSkipMemory(image)
}

@BindingAdapter("android:urlByGlide")
fun ImageView.urlByGlide(uri: Uri?) {
    val tag = getTag(tagGlide)
    if (tag == null || tag != uri)
        loadUri(uri)
}

@BindingAdapter("android:resource")
fun ImageButton.resource(image: Int) {
    setImageResource(image)
}

@BindingAdapter("android:middleLine")
fun TextView.middleLine(middle: Boolean) {
    if (middle)
        paint.flags = Paint.STRIKE_THRU_TEXT_FLAG
}

@BindingAdapter("android:underline")
fun TextView.underline(withUnderline: Boolean) {
    if (withUnderline) {
        paint.apply {
            isAntiAlias = true
        }
        paintFlags = paintFlags.or(Paint.UNDERLINE_TEXT_FLAG)
    }
}

@BindingAdapter("android:numFormat")
fun TextView.numFormat(num: Double) {
    text = num.format()
}

@BindingAdapter(value = ["android:dateValue", "android:dateFormat"], requireAll = false)
fun TextView.dateFormat(dateValue: Long, dateFormat: String = "yyyy-MM-dd") {
    text = SimpleDateFormat(dateFormat, Locale.getDefault()).format(Date(dateValue))
}

@BindingAdapter(
    value = ["android:money", "android:positiveColor", "android:negativeColor"],
    requireAll = true
)
fun TextView.moneyFormat(
    money: Number,
    @ColorRes positiveColor: Int,
    @ColorRes negativeColor: Int
) {
    val num = when (money) {
        is Double -> money
        is Float -> money.toDouble()
        is Long -> money.toDouble().div(100)
        is Int -> money.toDouble().div(100)
        is Short -> money.toDouble().div(100)
        else -> money.toDouble()
    }
//    val positiveColor =
//        if (0 == positiveColor) context.attrColor(R.attr.colorPrimary) else positiveColor
//    val negativeColor = if (0 == negativeColor) Color.RED else negativeColor
    val isPositive: Boolean = num >= 0
    val sign = if (isPositive) "+" else ""
    setTextColor(ContextCompat.getColor(context, if (isPositive) positiveColor else negativeColor))
    text = "$sign$num"
}

@BindingAdapter("android:focus")
fun View.focus(focus: MutableLiveData<Boolean>) {
    this.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
        focus.value.let {
            if (hasFocus != it) {
                focus.value = hasFocus
            }
        }
    }
    focus.value?.let {
        if (hasFocus() != it) {
            if (it) {
                requestFocus()
            } else {
                clearFocus()
            }
        }
    }
}

@BindingAdapter("android:onImeAction")
fun EditText.onImeAction(text: MutableLiveData<String>) {
    val imeOptions = imeOptions
    setOnEditorActionListener { _: TextView?, actionId: Int, _: KeyEvent? ->
        if (imeOptions == actionId) {
            text.value = this.text.toString()
            return@setOnEditorActionListener true
        }
        false
    }
}

@BindingAdapter("android:onImeOptClick")
fun EditText.onImeOptClick(onClickListener: View.OnClickListener) {
    val imeOptions = imeOptions
    setOnEditorActionListener { view: TextView, actionId: Int, _: KeyEvent? ->
        if (imeOptions == actionId) {
            onClickListener.onClick(view)
            return@setOnEditorActionListener true
        }
        false
    }
}

@BindingAdapter(
    value = ["android:resArrayId", "android:itemLayoutId", "android:dropLayoutId", "android:selectedPosition"],
    requireAll = false
)
fun Spinner.resArrayId(
    texts: Array<String>,
    @LayoutRes itemLayoutId: Int,
    @LayoutRes dropDownItemLayoutId: Int,
    index: MutableLiveData<Int>
) {
    if (null == adapter) {
        val itemLayout =
            if (0 != itemLayoutId) itemLayoutId else android.R.layout.simple_spinner_item
        val dropDownItemLayout =
            if (0 != dropDownItemLayoutId) dropDownItemLayoutId else android.R.layout.simple_spinner_dropdown_item
        ArrayAdapter(context, itemLayout, texts).also { arrayAdapter ->
            arrayAdapter.setDropDownViewResource(dropDownItemLayout)
            adapter = arrayAdapter
        }
    }
    index.value?.let {
        if (it != selectedItemPosition) {
            setSelection(it)
        }
    }
    if (null == onItemSelectedListener) {
        onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (index.value != position) {
                    index.value = position
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
    }
}

@BindingAdapter("android:onItemSelected")
fun Spinner.onItemSelected(onItemSelected: (Int, String) -> Unit) {
    onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            if (view is TextView) {
                onItemSelected(position, view.text.toString())
            }
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {

        }

    }
}

@BindingAdapter("android:enableLoadMore")
fun SmartRefreshLayout.enableLoadMore(isLoadMoreEnable: Boolean) {
    setEnableLoadMore(isLoadMoreEnable)
}

@BindingAdapter("android:enableRefresh")
fun SmartRefreshLayout.enableRefresh(isRefreshEnable: Boolean) {
    setEnableRefresh(isRefreshEnable)
}

@BindingAdapter("android:boldOrNormal")
fun TextView.boldOrNormal(bold: Boolean) {
    setTypeface(Typeface.DEFAULT, if (bold) Typeface.BOLD else Typeface.NORMAL)
}

@BindingAdapter("android:loadHtml")
fun WebView.loadHtml(content: String?) {
    if (null != content) {
        loadDataWithBaseURL(
            null,
            content,
            "text/html",
            "utf-8",
            null
        )
    }
}

@BindingAdapter("android:imageTint")
fun ImageView.imageTint(color: Any) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && color is ColorStateListDrawable) color.colorStateList else when (color) {
        is ColorDrawable -> ColorStateList.valueOf(color.color)
        is Long -> ColorStateList.valueOf(color.toInt())
        is Int -> ColorStateList.valueOf(color.toInt())
        else -> null
    }
        ?.let(this::setImageTintList)
}

@BindingAdapter("android:paddingHorizontalDp")
fun View.paddingHorizontalDp(padding: Float) {
    setPadding(padding.toInt(), 0, padding.toInt(), 0)
}