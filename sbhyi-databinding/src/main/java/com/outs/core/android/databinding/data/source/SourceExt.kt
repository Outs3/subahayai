package com.outs.core.android.databinding.data.source

/**
 * author: Outs3
 * e-mail: 3.3nosekai@gmail.com
 * date: 2021/7/8 13:42
 * desc:
 */
fun <T> newListDataSource(get: suspend () -> MutableList<T>): ListDataSource<T> =
    object : ListDataSource<T>() {
        override suspend fun requestData(): MutableList<T> = get()
    }

fun <T> newPageDataSource(
    pageLimit: Int = 20,
    get: suspend (pageNo: Int, pageLimit: Int) -> MutableList<T>
): PageDataSource<T> =
    object : PageDataSource<T>() {
        init {
            mPageSize = pageLimit
        }

        override suspend fun requestData(): MutableList<T> = get(mPage, mPageSize)
    }
