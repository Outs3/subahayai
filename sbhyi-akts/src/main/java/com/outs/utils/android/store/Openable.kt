package com.outs.utils.android.store

import android.provider.OpenableColumns

/**
 * author: Outs3
 * e-mail: 3.3nosekai@gmail.com
 * date: 2022/1/18 16:38
 * desc:
 */
data class Openable(
    @Column(name = OpenableColumns.DISPLAY_NAME)
    val displayName: String? = null,
    @Column(name = OpenableColumns.SIZE)
    val size: Long? = null,
) : IRow