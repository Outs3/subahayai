package com.outs.utils.kotlin

import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method
import java.lang.reflect.Proxy

/**
 * author: Outs3
 * e-mail: 3.3nosekai@gmail.com
 * date: 2021/12/17 10:42
 * desc: 代理
 */
private inline fun <reified T : Any> proxyOfOne(one: T): T = ProxyOne(T::class.java, one).proxy

class ProxyOne<T : Any>(interfaceCls: Class<T>, val one: T) : InvocationHandler {

    @Suppress("UNCHECKED_CAST")
    val proxy: T = Proxy.newProxyInstance(
        one.javaClass.classLoader,
        arrayOf(interfaceCls),
        this
    ) as T

    override fun invoke(proxy: Any?, method: Method?, args: Array<out Any?>): Any? {
        return method?.invoke(one, *args)
    }

}

inline fun <reified T : Any> proxyOfList(list: List<T>) =
    SimpleProxy(
        list.first().javaClass.classLoader,
        T::class.java
    ).also { it.addAll(list) }.proxy.typeOf<T>()

inline fun <reified T> simpleProxy(): SimpleProxy<T> =
    SimpleProxy(T::class.java.classLoader, T::class.java)

inline fun <reified T> SimpleProxy<T>.bindTo(target: (T) -> Unit) {
    proxy.typeOfOrNull<T>()?.also(target)
}

class SimpleProxy<T>(classLoader: ClassLoader, cls: Class<T>) : InvocationHandler {

    private val data: ArrayList<T> by lazy { ArrayList() }

    val proxy by lazy {
        Proxy.newProxyInstance(
            classLoader,
            arrayOf(cls),
            this
        )
    }

    override fun invoke(proxy: Any?, method: Method?, args: Array<out Any>?): Any? {
        var ret: Any? = null
        data.forEach {
            ret = method?.invoke(it, args)
        }
        return ret
    }

    fun add(item: T) = data.add(item)

    fun addAll(iterator: Iterable<T>) {
        data.addAll(iterator)
    }

    fun remove(item: T) = data.remove(item)

    @Suppress("UNCHECKED_CAST")
    fun bind(bindTo: (T) -> Unit) {
        bindTo(proxy as T)
    }

}
