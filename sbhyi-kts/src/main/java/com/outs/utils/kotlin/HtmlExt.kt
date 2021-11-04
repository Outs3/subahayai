package com.outs.utils.kotlin

/**
 * author: Outs3
 * e-mail: 3.3nosekai@gmail.com
 * date: 2021/4/24 14:21
 * desc:
 */
fun String.asVideoTag() =
    "<video src=\"$this\" style=\"margin-left: 10%;width: 80%;\">您的浏览器不支持 video 标签。</video>"