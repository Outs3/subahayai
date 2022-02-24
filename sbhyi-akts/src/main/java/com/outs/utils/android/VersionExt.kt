package com.outs.utils.android

import android.os.Build

/**
 * author: Outs3
 * e-mail: 3.3nosekai@gmail.com
 * date: 2022/2/22 14:11
 * desc:
 */
fun requireSdkVersion(requireSdkVersion: Int): Boolean? =
    if (Build.VERSION.SDK_INT >= requireSdkVersion) true else null