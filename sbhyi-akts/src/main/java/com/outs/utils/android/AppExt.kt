package com.outs.utils.android

import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.Utils

/**
 * author: Outs3
 * e-mail: 3.3nosekai@gmail.com
 * date: 2021/6/3 16:50
 * desc:
 */
val appInstance by lazy { Utils.getApp() }

val isDebug by lazy { AppUtils.isAppDebug() }