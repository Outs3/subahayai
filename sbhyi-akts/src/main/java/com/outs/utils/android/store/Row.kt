package com.outs.utils.android.store

import android.database.Cursor
import com.outs.utils.kotlin.tryOr
import java.lang.reflect.Field
import java.lang.reflect.Type

/**
 * author: Outs3
 * e-mail: 3.3nosekai@gmail.com
 * date: 2021/4/13 13:48
 * desc:
 */
interface IRow

data class ColumnField(
    val columnIndex: Int,
    val columnName: String,
    val type: Type,
    val field: Field,
) {
    override fun toString(): String {
        return "ColumnField(columnIndex=$columnIndex, columnName='$columnName', type=$type, field=$field)"
    }
}

fun Cursor.readByType(columnIndex: Int, type: Type): Any? =
    if (isNull(columnIndex)) null else when (type) {
        Int::class.java, Int::class.javaObjectType -> getInt(columnIndex)
        Long::class.java, Long::class.javaObjectType -> getLong(columnIndex)
        String::class.java -> getString(columnIndex)
        Float::class.java, Float::class.javaObjectType -> getFloat(columnIndex)
        Double::class.java, Double::class.javaObjectType -> getDouble(columnIndex)
        Short::class.java, Short::class.javaObjectType -> getShort(columnIndex)
        ByteArray::class.java -> getBlob(columnIndex)
        else -> null
    }

inline fun <reified T : IRow> Cursor.readRows(): List<T> {
    val ret = ArrayList<T>(count)
    if (0 != count && moveToFirst()) {
        val cls = T::class.java
        val fields = cls.declaredFields
            .mapNotNull { field ->
                val column = field.getAnnotation(Column::class.java) ?: return@mapNotNull null
                val index = if (-1 != column.index) column.index else getColumnIndex(column.name)
                if (-1 == index) return@mapNotNull null
                field.isAccessible = true
                ColumnField(index, column.name, field.genericType, field)
            }
        do {
            tryOr {
                val item = cls.newInstance()
                fields.forEach { it.field.set(item, readByType(it.columnIndex, it.type)) }
                ret.add(item)
            }
        } while (moveToNext())
    }
    return ret
}