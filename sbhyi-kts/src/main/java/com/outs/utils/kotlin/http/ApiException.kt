package com.outs.utils.kotlin.http

/**
 * author: Outs3
 * e-mail: 3.3nosekai@gmail.com
 * date: 2021/6/4 13:18
 * desc:
 */
class ApiException(val code: Int, val msg: String?, val body: Any? = null) :
    RuntimeException("code: $code msg:${msg ?: ""}")