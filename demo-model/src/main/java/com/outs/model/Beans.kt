package com.outs.model

/**
 * author: Outs3
 * e-mail: 3.3nosekai@gmail.com
 * date: 2022/3/21 15:01
 * desc:
 */

const val SEX_FEMALE = 0
const val SEX_MALE = 1

data class User(
    val id: Long,
    val name: String,
    val sex: Int? = null,
    val avatar: String? = null,
    val birthday: Long? = null,
    val phone: String? = null,
)
