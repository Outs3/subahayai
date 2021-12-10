package com.outs.utils.android

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.outs.utils.kotlin.launchOn
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

/**
 * author: Outs3
 * e-mail: 3.3nosekai@gmail.com
 * date: 2021/9/27 9:46
 * desc:
 */
inline fun <reified T> MutableLiveData<T>.getOrDefault(default: T): T = value ?: default

inline fun <reified T> MutableLiveData<T>.getOrDefault(default: () -> T): T = value ?: default()

inline fun <reified T> LiveData<T>.observeNotNullForever(crossinline block: (T) -> Unit) {
    observeForever { it?.let(block) }
}

fun <T> MutableLiveData<T>.setOnNot(newValue: T?): T? {
    if (value != newValue) {
        value = newValue
    }
    return newValue
}

fun <T> MutableLiveData<T>.postOnNot(newValue: T?): T? {
    if (value != newValue) {
        postValue(newValue)
    }
    return newValue
}

fun <T> MutableLiveData<T>.update(
    post: Boolean = false,
    onNull: T? = null,
    update: T.() -> Unit = {}
): T? {
    val newValue = value?.apply(update) ?: onNull
    if (post) postValue(newValue) else setValue(newValue)
    return newValue
}

fun MutableLiveData<Boolean>.toggle(post: Boolean = false, onNull: Boolean? = null): Boolean? {
    val newValue = value?.not() ?: onNull
    if (post) postValue(newValue) else setValue(newValue)
    return newValue
}

fun MutableLiveData<Int>.toggle(post: Boolean = false, onZero: Int = 1): Int {
    val newValue = value.let { if (null == it || 0 == it) onZero else 0 }
    if (post) postValue(newValue) else setValue(newValue)
    return newValue
}

fun MutableLiveData<Int>.inc(post: Boolean = false): Int? = value?.inc()?.also { newValue ->
    if (post) postValue(newValue) else setValue(newValue)
}

fun MutableLiveData<Int>.dec(post: Boolean = false): Int? = value?.dec()?.also { newValue ->
    if (post) postValue(newValue) else setValue(newValue)
}

@Deprecated(message = "replace with mapTo", replaceWith = ReplaceWith("mapTo"))
fun <I, O> LiveData<I>.trans(mapper: MediatorLiveData<O>.(I?) -> Unit): MediatorLiveData<O> =
    MediatorLiveData<O>().apply {
        addSource(this@trans) {
            mapper(it)
        }
    }

@Deprecated(message = "replace with mapTo", replaceWith = ReplaceWith("mapTo"))
fun <I, O> LiveData<I>.transWithSet(
    setOnInputNull: Boolean = true,
    setOnOutputNull: Boolean = true,
    setOrPost: Boolean = true,
    mapper: (I?) -> O?
): MediatorLiveData<O> =
    MediatorLiveData<O>().apply {
        addSource(this@transWithSet) { input ->
            if (null != input || setOnInputNull) {
                val output = mapper(input)
                if (null != output || setOnOutputNull) {
                    val callApi = if (setOrPost) ::setOnNot else ::postOnNot
                    callApi.invoke(output)
                }
            }
        }
    }

fun <I, O> LiveData<I>.mapTo(
    setOnInputNull: Boolean = true,
    setOnOutputNull: Boolean = true,
    setOrPost: Boolean = true,
    mapper: (I?) -> O?
): MediatorLiveData<O> =
    MediatorLiveData<O>().apply {
        addSource(this@mapTo) { input ->
            if (null != input || setOnInputNull) {
                val output = mapper(input)
                if (null != output || setOnOutputNull) {
                    val callApi = if (setOrPost) ::setOnNot else ::postOnNot
                    callApi.invoke(output)
                }
            }
        }
    }

fun <I, O> LiveData<I>.suspendMapTo(
    setOnInputNull: Boolean = true,
    setOnOutputNull: Boolean = true,
    context: CoroutineContext = Dispatchers.IO,
    setOrPost: Boolean = false,
    mapper: suspend (I?) -> O?
): MediatorLiveData<O> =
    MediatorLiveData<O>().apply {
        addSource(this@suspendMapTo) { input ->
            if (null != input || setOnInputNull) {
                launchOn(CoroutineScope(context)) {
                    val output = mapper(input)
                    if (null != output || setOnOutputNull) {
                        val callApi = if (setOrPost) ::setOnNot else ::postOnNot
                        callApi.invoke(output)
                    }
                }
            }
        }
    }
