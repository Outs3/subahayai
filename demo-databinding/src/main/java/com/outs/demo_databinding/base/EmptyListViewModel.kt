package com.outs.demo_databinding.base

import com.outs.core.android.databinding.data.source.DataSource
import com.outs.core.android.databinding.data.source.DataSourceFactory


/**
 * author: Outs3
 * e-mail: 3.3nosekai@gmail.com
 * date: 2021/9/7 16:17
 * desc:
 */
class EmptyListViewModel : ListViewModel<Unit>() {
    override fun getDataSource(): DataSource<Unit> = DataSourceFactory.empty()
}