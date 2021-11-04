package com.outs.core.android.databinding.holder

import android.content.Context
import android.content.res.Resources
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.outs.core.android.databinding.adapter.SimpleItem
import com.outs.core.android.databinding.inflateDataBinding
import com.outs.utils.android.onClickOrRepeat
import com.outs.utils.kotlin.method
import com.outs.utils.kotlin.tryOrNull
import kotlinx.android.extensions.LayoutContainer
import java.lang.ref.WeakReference


/**
 * author: Outs3
 * e-mail: 3.3nosekai@gmail.com
 * date: 2021/4/11 21:05
 * desc:
 */
open class BaseViewHolder<T : ViewDataBinding>(val mBinding: T) :
    RecyclerView.ViewHolder(mBinding.root)

interface OnModelClickListener<T> {
    fun onModelClick(position: Int, model: T, view: View? = null)
}

fun <T> ((T) -> Unit).asOnModelClick(): OnModelClickListener<T> =
    object : OnModelClickListener<T> {
        override fun onModelClick(position: Int, model: T, view: View?) {
            this@asOnModelClick(model)
        }
    }

fun <T> ((Int, T) -> Unit).asOnModelClick(): OnModelClickListener<T> =
    object : OnModelClickListener<T> {
        override fun onModelClick(position: Int, model: T, view: View?) {
            this@asOnModelClick(position, model)
        }
    }

fun <T> ((Int, T, View?) -> Unit).asOnModelClick(): OnModelClickListener<T> =
    object : OnModelClickListener<T> {
        override fun onModelClick(position: Int, model: T, view: View?) {
            this@asOnModelClick(position, model, view)
        }
    }

abstract class DataBindingViewHolder<D, VDB : ViewDataBinding>(var onModelClick: OnModelClickListener<D>? = null) :
    SimpleItem<D>(), LayoutContainer, View.OnClickListener {

    protected val context: Context get() = mBinding.root.context

    protected val resources: Resources get() = mBinding.root.resources

    lateinit var mBinding: VDB

    lateinit var mHolder: WeakReference<RecyclerView.ViewHolder>

    override val containerView: View
        get() = mBinding.root

    override fun setViews() {
        super.setViews()
        mBinding.root.onClickOrRepeat { onClick(it) }
    }

    override fun getItemView(context: Context, parent: ViewGroup?): View {
        mBinding = getLayoutId.inflateDataBinding(context, parent, false)
        return mBinding.root
    }

    override fun onClick(v: View?) {
        onModelClick?.onModelClick(v)
    }

    fun OnModelClickListener<D>.onModelClick(v: View? = null) {
        model?.let { onModelClick(position, it, v) }
    }

    fun updateSelf() {
        mHolder.get()
            ?.takeIf { -1 != position }
            ?.bindingAdapter
            ?.notifyItemChanged(position)
    }

}

abstract class CheckableViewHolder<D : Any, VDB : ViewDataBinding> :
    DataBindingViewHolder<D, VDB>() {
    var checkedParent: ICheckedParent<D>? = null

    val isChecked: Boolean get() = model?.let { checkedParent?.isChecked(it) } ?: false

    override fun setViews() {
        super.setViews()
        mBinding.root.onClickOrRepeat { checkThis() }
    }

    open fun checkThis() {
        checkedParent?.updateChecked(model)
    }
}

interface ICheckedParent<T> {
    fun updateChecked(new: T?, withCallback: Boolean = true)
    fun isChecked(item: T): Boolean
}

open class AutoItem<T : Any, VDB : ViewDataBinding>(
    @LayoutRes private val layoutId: Int,
    onModelClick: OnModelClickListener<T>? = null
) :
    DataBindingViewHolder<T, VDB>(onModelClick) {

    private var doOnCreate: ((VDB) -> Unit)? = null

    private var doOnBinding: ((VDB, T?, Int) -> Unit)? = null

    override val getLayoutId: Int
        get() = layoutId

    override fun setViews() {
        super.setViews()
        doOnCreate?.invoke(mBinding)
    }

    override fun handleData(model: T?, position: Int) {
        super.handleData(model, position)
        tryOrNull {
            mBinding.method("setPosition", false, Int::class.java)?.invoke(mBinding, position)
        }
        if (null != model) tryOrNull {
            mBinding.method("setModel", false, model.javaClass)?.invoke(mBinding, model)
        }
        tryOrNull {
            mBinding.method("setOnClick", false, View.OnClickListener::class.java)
                ?.invoke(mBinding, this)
        }
        doOnBinding?.invoke(mBinding, model, position)
    }

    fun doOnCreate(doOnCreate: ((VDB) -> Unit)? = null) = also { it.doOnCreate = doOnCreate }

    fun doOnBinding(doOnBinding: ((VDB, T?, Int) -> Unit)? = null) =
        also { it.doOnBinding = doOnBinding }

}

open class CheckedAutoItem<T : Any, VDB : ViewDataBinding>(
    @LayoutRes private val layoutId: Int
) : CheckableViewHolder<T, VDB>() {

    override val getLayoutId: Int
        get() = layoutId

    override fun handleData(model: T?, position: Int) {
        super.handleData(model, position)
        tryOrNull {
            mBinding.method("setIsChecked", false, Boolean::class.java)?.invoke(mBinding, isChecked)
        }
        tryOrNull {
            mBinding.method("setPosition", false, Int::class.java)?.invoke(mBinding, position)
        }
        if (null != model)
            tryOrNull {
                mBinding.method("setModel", false, model.javaClass)?.invoke(mBinding, model)
            }
        tryOrNull {
            mBinding.method("setOnClick", false, View.OnClickListener::class.java)
                ?.invoke(mBinding, this)
        }
    }

}