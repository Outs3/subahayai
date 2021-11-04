package com.outs.utils.android.store

import android.provider.CallLog

/**
 * author: Outs3
 * e-mail: 3.3nosekai@gmail.com
 * date: 2021/4/13 14:17
 * desc:
 */
data class Call(
    @Column(name = CallLog.Calls._ID)
    val id: Int? = null,
    @Column(name = CallLog.Calls.NUMBER)
    val number: String? = null,
    @Column(name = CallLog.Calls.TYPE)
    val type: Int? = null,
    @Column(name = CallLog.Calls.DURATION)
    val duration: Int? = null,
    @Column(name = CallLog.Calls.DATE)
    val date: Long? = null
) : IRow