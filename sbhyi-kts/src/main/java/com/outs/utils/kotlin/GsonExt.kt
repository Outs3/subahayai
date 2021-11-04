package com.outs.utils.kotlin

import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken

/**
 * author: Outs3
 * e-mail: 3.3nosekai@gmail.com
 * date: 2021/4/11 23:29
 * desc:
 */
val globalGson = GsonBuilder().serializeNulls().disableHtmlEscaping().create()

fun <R> String.asObj(type: Class<R>): R =
    globalGson.fromJson(this, TypeToken.get(type).type)

inline fun <reified R> String.asObj(): R =
    globalGson.fromJson(this, object : TypeToken<R>() {}.type)

inline fun <reified R> String.tryAsObj(): R? =
    try {
        globalGson.fromJson(this, object : TypeToken<R>() {}.type)
    } catch (e: Throwable) {
        null
    }

fun Any.toJson(): String = globalGson.toJson(this)

fun JsonObject.getAsString(memberName: String): String = get(memberName).asString

fun JsonObject.getAsStringOrNull(memberName: String): String? = get(memberName)?.asString