package com.outs.core.android.databinding.adapter

/**
 * @author Jack Tony
 * @date 2015/8/29
 */
class ItemTypeUtil {

    private var typePool: MutableMap<Any, Int>? = null

    fun setTypePool(typePool: MutableMap<Any, Int>) {
        this.typePool = typePool
    }

    /**
     * @param type item的类型
     * @return 通过object类型的type来得到int类型的type
     */
    fun getIntType(type: Any): Int {
        if (typePool == null) {
            typePool = mutableMapOf()
        }

        if (!typePool!!.containsKey(type)) {
            typePool!![type] = typePool!!.size
        }
        return typePool!![type]!!
    }
}
