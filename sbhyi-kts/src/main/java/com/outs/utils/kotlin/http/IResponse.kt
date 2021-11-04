package com.outs.utils.kotlin.http

/**
 * author: Outs3
 * e-mail: 3.3nosekai@gmail.com
 * date: 2021/10/29 15:52
 * desc:
 */
interface IResponse {
    fun code(): Int
    fun message(): String
    fun isSuccess(): Boolean

    fun successOrThrow() {
        if (!isSuccess()) throw ApiException(code(), message())
    }

}