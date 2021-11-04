package com.outs.core.android.databinding.data.source

import androidx.lifecycle.*
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.ViewUtils
import com.outs.utils.android.doOnDestroy
import com.outs.utils.kotlin.launchOnIO
import com.outs.utils.kotlin.reset
import java.lang.ref.WeakReference
import java.util.*
import kotlin.collections.ArrayList

/**
 * author: Outs3
 * e-mail: 3.3nosekai@gmail.com
 * date: 2021/4/10 23:32
 * desc:
 */
open class DataSource<T> {

    private val onDataChangedListeners by lazy { LinkedList<() -> Unit>() }

    private val adapters by lazy { ArrayList<WeakReference<RecyclerView.Adapter<*>>>() }

    val position = MutableLiveData(0)

    val data: MutableList<T> = ArrayList()

    val isEmpty = MutableLiveData<Boolean>()

    fun addListener(onDataChangedListener: () -> Unit) {
        onDataChangedListeners.add(onDataChangedListener)
    }

    fun removeListener(onDataChangedListener: () -> Unit) {
        onDataChangedListeners.remove(onDataChangedListener)
    }

    fun addAdapter(adapter: RecyclerView.Adapter<*>, lifecycle: Lifecycle? = null) {
        adapters.add(WeakReference(adapter))
        lifecycle?.doOnDestroy { removeAdapter(adapter) }
    }

    fun removeAdapter(adapter: RecyclerView.Adapter<*>) {
        adapters.removeAll { it.get() == adapter }
    }

    open fun add(data: T) {
        val position = this.data.size
        this.data.add(data)
        notifyAdapters { adapter -> adapter.notifyItemInserted(position) }
    }

    open fun addOnIO(data: T) {
        launchOnIO {
            add(data)
        }
    }

    open fun addAll(data: Iterable<T>) {
        val position = this.data.size
        this.data.addAll(data)
        val count = this.data.size - position
        notifyAdapters { adapter -> adapter.notifyItemRangeInserted(position, count) }
    }

    open fun remove(data: T) {
        val position = this.data.indexOf(data)
        if (-1 != position) {
            this.data.remove(data)
            notifyAdapters { adapter -> adapter.notifyItemRemoved(position) }
        }
    }

    open fun update(data: T) {
        val position = this.data.indexOf(data)
        if (-1 != position) {
            notifyAdapters { adapter -> adapter.notifyItemChanged(position) }
        }
    }

    open fun removeAll(data: Iterable<T>) {
        this.data.removeAll(data)
        notifyAdapters()
    }

    open fun clear() {
        this.data.clear()
        notifyAdapters()
    }

    open fun reset(data: List<T>) {
        this.data.reset(data)
        notifyAdapters()
    }

    open fun calcEmptyAndPosition() {
        isEmpty.postValue(this.data.isEmpty())
        if (position.value!! >= data.size) {
            position.postValue(data.size - 1)
        }
    }

    open fun isBindToAdapter() = adapters.isNotEmpty()

    open fun notifyAdapters(action: (RecyclerView.Adapter<*>) -> Unit = RecyclerView.Adapter<*>::notifyDataSetChanged) {
        calcEmptyAndPosition()
        ViewUtils.runOnUiThread {
            adapters.mapNotNull(WeakReference<RecyclerView.Adapter<*>>::get).forEach(action)
            onDataChangedListeners.forEach { it.invoke() }
        }
    }

    open fun getPosition() = position.value!!

    open fun setPosition(position: Int) {
        this.position.value = position
    }

    companion object {
        fun <T> empty(): DataSource<T> = object : DataSource<T>() {}

        fun <T> fromArray(data: Array<T>): DataSource<T> = object : DataSource<T>() {}.also {
            it.data.addAll(data)
        }

        fun <T> fromIterable(data: Iterable<T>): DataSource<T> = object : DataSource<T>() {}.also {
            it.data.addAll(data)
        }
    }

}