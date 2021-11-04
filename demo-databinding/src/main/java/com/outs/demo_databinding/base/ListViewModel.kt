package com.outs.demo_databinding.base

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.outs.core.android.databinding.data.source.DataSource
import com.outs.core.android.databinding.data.source.ListDataSource
import com.outs.core.android.vm.BaseViewModel
import com.outs.utils.kotlin.typeOfOrNull

/**
 * author: Outs3
 * e-mail: 3.3nosekai@gmail.com
 * date: 2021/9/2 9:21
 * desc:
 */
abstract class ListViewModel<T> : BaseViewModel() {

    val filterText by lazy { MutableLiveData<String>() }

    val filterType by lazy { MutableLiveData<String>() }

    val sortType by lazy { MutableLiveData<String>() }

    val data by lazy { getDataSource() }

    abstract fun getDataSource(): DataSource<T>

    override fun observe(owner: LifecycleOwner) {
        super.observe(owner)
        filterText.observe(owner) { refresh() }
        filterType.observe(owner) { refresh() }
        sortType.observe(owner) { refresh() }
    }

    open fun refresh() = data
        .takeIf(DataSource<*>::isBindToAdapter)
        ?.typeOfOrNull<ListDataSource<*>>()
        ?.takeIf { false == it.isLoading.value }
        ?.refresh()
        .also {
//            "${javaClass.simpleName} filterType: ${filterType.value} filterText: ${filterText.value} sortType: ${sortType.value}".dToast()
        }

}