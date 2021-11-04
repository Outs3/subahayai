package com.outs.utils.kotlin.http

/**
 * author: Outs3
 * e-mail: 3.3nosekai@gmail.com
 * date: 2021/10/29 15:54
 * desc:
 */
interface IDataResponse<T> : IResponse {
    fun data(): T

    fun dataOrThrow(): T {
        successOrThrow()
        return data()
    }

}