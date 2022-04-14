package com.outs.utils.kotlin.http

import okhttp3.MediaType
import okhttp3.RequestBody
import okio.BufferedSink

/**
 * author: Outs3
 * e-mail: 3.3nosekai@gmail.com
 * date: 2022/4/14 10:31
 * desc:
 */
class CountingRequestBody(protected val delegate: RequestBody) : RequestBody() {
    override fun contentType(): MediaType? = delegate.contentType()

    override fun contentLength(): Long = delegate.contentLength()

    override fun writeTo(sink: BufferedSink) {
        TODO("Not yet implemented")
    }

    override fun isDuplex(): Boolean = delegate.isDuplex()

    override fun isOneShot(): Boolean = delegate.isOneShot()
}
