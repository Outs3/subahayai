package com.outs.utils.kotlin

import java.lang.ref.WeakReference
import java.lang.reflect.Field
import java.lang.reflect.Method
import kotlin.reflect.KClass
import kotlin.reflect.KType

/**
 * author: Outs3
 * e-mail: 3.3nosekai@gmail.com
 * date: 2021/4/2 15:54
 * desc: 反射工具类
 */
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class FieldName(val name: String = "", val isAccessible: Boolean = false)

interface Ref<T> {
    fun get(): T?

    fun set(value: T?)

    fun getNotNull(onGet: (T) -> Unit) {
        get()?.let(onGet)
    }
}

class FieldRef<T>(val field: Field, val target: WeakReference<Any>) : Ref<T> {

    constructor(field: Field, target: Any) : this(field, WeakReference(target))

    override fun get(): T? = tryOrNull { field.get(target.get()) as T? }

    override fun set(value: T?) {
        tryOr { field.set(target.get(), value) }
    }
}

fun Any.fields(withSuper: Boolean = true, filter: (Field) -> Boolean = { true }): List<Field> =
    HashSet<Field>().also { set ->
        fun checkAndAdd(field: Field) {
            if (!set.contains(field) && filter(field)) set.add(field)
        }

        fun Class<*>.checkAndAdd() {
            fields.forEach(::checkAndAdd)
            declaredFields.forEach(::checkAndAdd)
        }

        var cls: Class<*>? = javaClass
        do {
            cls?.checkAndAdd()
            cls = cls?.superclass
        } while (withSuper && null != cls)

    }.toList()

fun Any.field(name: String, isAccessible: Boolean = true): Field? =
    this.javaClass.field(name, isAccessible)

fun Class<*>.field(name: String, isAccessible: Boolean = true): Field? =
    field(name)?.apply {
        if (!this.isAccessible && isAccessible) this.isAccessible = true
    }

fun Class<*>.field(name: String): Field? =
    tryOr { getField(name) } ?: tryOr { getDeclaredField(name) } ?: superclass?.field(name)

fun Any.method(
    name: String,
    isAccessible: Boolean = true,
    vararg argTypes: Class<*>
): Method? =
    this.javaClass.method(name, isAccessible, *argTypes)

fun Class<*>.method(
    name: String,
    isAccessible: Boolean = true,
    vararg argTypes: Class<*>
): Method? =
    method(name, *argTypes)?.apply {
        if (!this.isAccessible && isAccessible) this.isAccessible = true
    }

fun Class<*>.method(name: String, vararg argTypes: Class<*>): Method? =
    tryOr { getMethod(name, *argTypes) } ?: tryOr { getDeclaredMethod(name, *argTypes) }
    ?: superclass?.method(name, *argTypes)

inline fun <reified R> Class<*>.refStatic(name: String, isAccessible: Boolean = true): R? =
    field(name, isAccessible)?.get(null) as R?

inline fun <reified R> Any.ref(name: String, isAccessible: Boolean = true): R? =
    field(name, isAccessible)?.get(this) as R?

inline fun <reified R> Any.set(name: String, value: R?, isAccessible: Boolean = true) =
    field(name, isAccessible)?.set(this, value)

//注意  由于自动封包问题 含基本数据类型参数的方法func 必须用method.invoke
inline fun <reified R> Any.func(
    name: String,
    isAccessible: Boolean = true,
    vararg args: Any
): R? =
    method(name, isAccessible, *args.map { it.javaClass }.toTypedArray())?.invoke(
        this,
        *args
    ) as R?

fun <T : Any, R> T.getFieldValue(name: String, isAccessible: Boolean = true): R? =
    field(name, isAccessible)?.get(this) as R?

fun <T : Any, R> T.callMethod(name: String, isAccessible: Boolean = true, vararg args: Any): R? =
    method(name, isAccessible, *args.map { it.javaClass }.toTypedArray())?.invoke(
        this,
        *args
    ) as R?

fun KType.of(value: String?) = when (this) {
    Int::class.java -> value?.toIntOrNull() ?: 0
    Long::class.java -> value?.toLongOrNull() ?: 0L
    Float::class.java -> value?.toFloatOrNull() ?: 0f
    Double::class.java -> value?.toDoubleOrNull() ?: 0.0
    Boolean::class.java -> value.toBoolean()
    String::class.java -> value
    else -> value
}

fun <T : Any> newInstanceByReflect(cls: KClass<T>, args: Map<String, String?>): T? =
    cls.constructors.firstOrNull()?.let { constructor ->
        constructor.callBy(constructor.parameters.associateWith { param -> param.type.of(args[param.name]) })
    }

fun Any.injectReflect() {
    this.fields { field -> null != field.getAnnotation(FieldName::class.java) }
        .forEach { field ->
            val anno = field.getAnnotation(FieldName::class.java)
            javaClass.superclass.field(anno.name.emptyTo(field.name), anno.isAccessible)
                ?.also { superField ->
                    superField.set(this, FieldRef<Any>(superField, this))
                }
        }
}