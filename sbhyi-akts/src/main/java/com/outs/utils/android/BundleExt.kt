package com.outs.utils.android

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import com.blankj.utilcode.util.GsonUtils
import com.outs.utils.android.intent.Extra
import com.outs.utils.kotlin.emptyTo
import com.outs.utils.kotlin.fields
import com.outs.utils.kotlin.tryAsObj
import java.io.Serializable
import java.lang.reflect.Field
import java.lang.reflect.Type

/**
 * author: Outs3
 * e-mail: 3.3nosekai@gmail.com
 * date: 2021/4/8 13:42
 * desc:
 */
fun Bundle.put(key: String, value: Any) {
    when (value) {
        is Int -> putInt(key, value)
        is Long -> putLong(key, value)
        is Float -> putFloat(key, value)
        is Double -> putDouble(key, value)
        is Boolean -> putBoolean(key, value)
        is String -> putString(key, value)
        is Serializable -> putSerializable(key, value)
        is Parcelable -> putParcelable(key, value)
        else -> throw RuntimeException("Type cannot be resolved!key: $key, value: $value")
    }
}

inline fun <reified T> Bundle.getByType(key: String): T? {
    val cls = T::class.java
    return when {
        !containsKey(key) -> null
        cls == Int::class.java -> getInt(key)
        cls == Long::class.java -> getLong(key)
        cls == Float::class.java -> getFloat(key)
        cls == Double::class.java -> getDouble(key)
        cls == Boolean::class.java -> getBoolean(key)
        cls == String::class.java -> getString(key)
        cls.interfaces.contains(Serializable::class.java) -> getSerializable(key)
        cls.interfaces.contains(Parcelable::class.java) -> getParcelable(key)
        else -> getString(key)?.tryAsObj()
    } as T?
}

fun Bundle.getByType(key: String, cls: Class<*>, type: Type): Any? {
    return when {
        !containsKey(key) -> null
        cls == Int::class.java -> getInt(key)
        cls == Long::class.java -> getLong(key)
        cls == Float::class.java -> getFloat(key)
        cls == Double::class.java -> getDouble(key)
        cls == Boolean::class.java -> getBoolean(key)
        cls == String::class.java -> getString(key)
        cls.interfaces.contains(Serializable::class.java) -> getSerializable(key)
        cls.interfaces.contains(Parcelable::class.java) -> getParcelable<Parcelable>(key)
        else -> getString(key)?.let { GsonUtils.getGson().fromJson(it, type) }
    }
}

fun Bundle.put(pair: Pair<String, Any>) {
    put(pair.first, pair.second)
}

fun Bundle.put(pair: Map.Entry<String, Any>) {
    put(pair.key, pair.value)
}

fun Pair<String, Any>.asBundle(): Bundle = Bundle().also {
    it.put(this)
}

fun Bundle.putAll(vararg pairs: Pair<String, Any>) {
    pairs.forEach { put(it) }
}

fun HashMap<String, Any>.asBundle(): Bundle = Bundle().also {
    this.forEach { entry -> it.put(entry) }
}

fun Bundle.asIntent() = Intent().putExtras(this)

fun Any.extraFields() = fields {
    if (!it.isAccessible) it.isAccessible = true
    null != it.getAnnotation(Extra::class.java)
}

fun Bundle.unpackExtra(extraField: Field) =
    extraField.getAnnotation(Extra::class.java)?.name?.emptyTo(extraField.name)?.let { key ->
        getByType(
            key,
            extraField.type,
            extraField.genericType
        )
    }

fun Any.inject(extras: Bundle? = null) {
    extras?.let { from ->
        this.extraFields()
            .map { field ->
                field to
                        try {
                            from.unpackExtra(field)?.also { value -> field.set(this, value) }
                        } catch (e: Exception) {
                            e
                        }
            }
            .bundleDebugLog("${javaClass.simpleName} Inject extra values: ")
    }
}

private fun <T> List<T>.bundleDebugLog(prefix: String = "") {
    if (isDebug)
        this.joinToString("\n\t")
            .let { "$prefix \n\t$it" }
            .d()
}
