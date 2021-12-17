package com.outs.utils.kotlin

/**
 * author: Outs3
 * e-mail: 3.3nosekai@gmail.com
 * date: 2021/4/17 9:05
 * desc:
 */
inline fun <reified T> T.let(predicate: Boolean, ret: T): T = if (predicate) ret else this

inline fun <reified T> Any?.typeOf(): T = this as T

inline fun <reified T> Any?.typeOfOrNull(): T? = if (this is T) this else null

inline fun <reified T, R : Throwable> T?.throwNull(asThrowable: () -> R): T =
    this ?: throw asThrowable()

inline fun <reified T> T?.throwNull(throwMessage: String): T =
    this ?: throw RuntimeException(throwMessage)

inline fun <reified T, R : Throwable> T.throwIf(predicate: Boolean, asThrowable: T.() -> R) =
    takeUnless { predicate } ?: throw asThrowable(this)

inline fun <reified T, R : Throwable> T.throwIf(
    predicate: T.() -> Boolean,
    asThrowable: T.() -> R
) =
    takeUnless { predicate(this) } ?: throw asThrowable(this)

inline fun <reified T, R : Throwable> T.throwUnless(predicate: Boolean, asThrowable: T.() -> R) =
    takeIf { predicate } ?: throw asThrowable(this)

inline fun <reified T, R : Throwable> T.throwUnless(
    predicate: T.() -> Boolean,
    asThrowable: T.() -> R
) =
    takeIf { predicate(this) } ?: throw asThrowable(this)

val Any.className: String get() = javaClass.simpleName