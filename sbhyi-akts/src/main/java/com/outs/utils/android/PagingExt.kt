package com.outs.utils.android

import androidx.paging.PagingSource
import androidx.paging.PagingState

/**
 * author: Outs3
 * e-mail: 3.3nosekai@gmail.com
 * date: 2022/3/18 15:58
 * desc:
 */
abstract class IntPagingSource<T : Any> : PagingSource<Int, T>() {
    override fun getRefreshKey(state: PagingState<Int, T>): Int? =
        state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, T> {
        val nextPageNumber = params.key ?: 1
        return try {
            val response = loadDataOrThrow(nextPageNumber)
            LoadResult.Page(
                data = response,
                prevKey = if (1 == nextPageNumber) null else nextPageNumber - 1,
                nextKey = if (response.isEmpty()) null else nextPageNumber + 1
            )
        } catch (e: Throwable) {
            LoadResult.Error(e)
        }
    }

    abstract suspend fun loadDataOrThrow(pageNum: Int): List<T>
}

inline fun <reified T : Any> intPagingSource(crossinline loadDataOrThrow: suspend (Int) -> List<T>) =
    object : IntPagingSource<T>() {
        override suspend fun loadDataOrThrow(pageNum: Int): List<T> = loadDataOrThrow(pageNum)
    }

