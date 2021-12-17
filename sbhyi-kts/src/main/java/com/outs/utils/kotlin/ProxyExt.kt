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
inline fun <reified T> proxyOfList(list: List<T>) =
    SimpleProxy(T::class.java).also { it.addAll(list) }.proxy.typeOf<T>()

inline fun <reified T> simpleProxy(): SimpleProxy<T> = SimpleProxy(T::class.java)

inline fun <reified T> SimpleProxy<T>.bindTo(target: (T) -> Unit) {
    proxy.typeOfOrNull<T>()?.also(target)
}

class SimpleProxy<T>(cls: Class<T>) : InvocationHandler {

    private val data: ArrayList<T> by lazy { ArrayList() }

    val proxy by lazy {
        Proxy.newProxyInstance(
            ClassLoader.getSystemClassLoader(),
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

    fun bind(bindTo: (T) -> Unit) {
        bindTo(proxy as T)
    }

}
