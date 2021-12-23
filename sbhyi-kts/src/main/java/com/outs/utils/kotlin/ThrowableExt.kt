package com.outs.utils.kotlin

import java.io.ByteArrayOutputStream
import java.io.PrintWriter

/**
 * author: Outs3
 * e-mail: 3.3nosekai@gmail.com
 * date: 2021/12/23 8:57
 * desc:
 */

/**
 * 将异常的打印堆栈内容转化为字符串
 */
fun Throwable.stackTraceAsString(): String =
    ByteArrayOutputStream()
        .also { PrintWriter(it).also(::printStackTrace).close() }
        .also(ByteArrayOutputStream::close)
        .toString()