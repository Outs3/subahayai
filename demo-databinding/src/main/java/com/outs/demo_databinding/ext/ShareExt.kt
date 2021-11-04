package com.outs.demo_databinding.ext

/**
 * author: Outs3
 * e-mail: 3.3nosekai@gmail.com
 * date: 2021/10/1 15:00
 * desc:
 */
private const val SUFFIX_INVITE_CODE = "inviteCode:"

fun buildInviteCode(userId: String) = "$SUFFIX_INVITE_CODE$userId"

fun isInviteCode(code: String) = code.startsWith(SUFFIX_INVITE_CODE)

fun getUserIdFromInviteCode(code: String): String = code.substringAfter(SUFFIX_INVITE_CODE)