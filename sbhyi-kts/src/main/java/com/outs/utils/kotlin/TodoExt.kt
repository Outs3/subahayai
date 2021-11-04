package com.outs.utils.kotlin

/**
 * author: Outs3
 * e-mail: 3.3nosekai@gmail.com
 * date: 2021/6/5 16:50
 * desc:
 */
//TODO: 这里的页面需要实现
fun String.toDoPage() = "TODO: 打开${this}页面".e()

//TODO: 这里的数据需要填充
fun String.toDoData() = "TODO: 填充${this}数据".e()

//TODO: 这里的功能需要实现
fun String.toDo() = "TODO: 实现${this}功能".e()

//TODO: 这里的功能需要实现
fun String.toDoApi() = "TODO: 接入${this}接口".e()

//这个类是虚构的，仅供调试使用，后期需要用实际的Bean取代
annotation class DebugBean

//这个数据是虚构的，模拟正常数据生成的，后期需要动态获取真实的数据取代
annotation class DebugData