package com.outs.utils.kotlin.http

import okhttp3.MediaType

/**
 * author: Outs3
 * e-mail: 3.3nosekai@gmail.com
 * date: 2023-04-19 15:32
 * desc:
 */
fun MediaType.isText(): Boolean = "text" == type ||
        "json" == subtype ||
        "xml" == subtype ||
        "html" == subtype ||
        "webviewhtml" == subtype
