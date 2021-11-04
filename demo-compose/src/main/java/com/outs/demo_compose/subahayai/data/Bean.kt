package com.outs.demo_compose.subahayai.data

import androidx.annotation.DrawableRes

/**
 * author: Outs3
 * e-mail: 3.3nosekai@gmail.com
 * date: 2021/10/11 16:24
 * desc:
 */
data class User(
    val id: Long,
    val name: String,
    @DrawableRes
    val icon: Int,
    val sign: String
)

abstract class BaseMessageContent(val id: Long, val type: Int, val desc: String)

class TextMessageContent(
    id: Long,
    type: Int,
    val content: String
) : BaseMessageContent(id, type, content)

data class Message(
    val id: Long,
    val fromUser: User,
    val to: User,
    val isSelf: Boolean,
    val content: BaseMessageContent
)
