package com.outs.demo_compose.subahayai.data

import androidx.annotation.DrawableRes
import com.outs.demo_compose.R
import kotlin.random.Random

/**
 * author: Outs3
 * e-mail: 3.3nosekai@gmail.com
 * date: 2021/10/11 16:24
 * desc:
 */
val randomBoolean: Boolean get() = Random.nextBoolean()

val randomIcon: Int @DrawableRes get() = R.drawable.loading

val randomLong: Long get() = Random.nextLong()

val randomName: String
    get() = listOf<String>(
        "张三",
        "李四",
        "王五",
        "徐六"
    ).random()

val randomString: String get() = "这是一条测试字符串"

val randomSign: String get() = "暂无签名"
