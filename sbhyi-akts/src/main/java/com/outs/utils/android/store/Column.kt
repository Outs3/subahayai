package com.outs.utils.android.store

/**
 * author: Outs3
 * e-mail: 3.3nosekai@gmail.com
 * date: 2021/4/13 13:43
 * desc:
 */
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class Column(
    val index: Int = -1,
    val name: String = ""
)
