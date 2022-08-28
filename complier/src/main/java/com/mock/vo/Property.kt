package com.mock.vo

import javax.lang.model.element.Element
import javax.lang.model.type.TypeKind
import javax.lang.model.type.TypeMirror

/**
 * @param element 类型信息
 * @param value 具体的值
 * @param isLast 是否是当前类的最后一个属性
 */
internal sealed class Property(
    val element: Element,
    val value: String,
    val isLast: Boolean,
) {

    override fun toString(): String {
        return "${element.simpleName} = $value"
    }

    open fun toLogString(): String {
        return "simpleName=${element.simpleName}, value=$value, isLast=${isLast}"
    }
}

internal fun List<Property>.equalsTo(other: List<String>): Boolean {
    if (this.size != other.size) {
        return false
    }
    for (i in this.indices) {
        if (this[i].value == com.mock.TODO) {
            continue
        }
        if (this[i].toString() != other[i]) {
            return false
        }
    }
    return true
}

/**
 * 原始属性，包括string
 */
internal class PrimitiveProperty(
    element: Element,
    value: String,
    isLast: Boolean,
) : Property(element, value, isLast)

/**
 * 自定义属性
 * @param isGrafted 是否被嫁接过，被嫁接过意味着该类型的所有case已经完善
 */
internal class DeclaredProperty(
    element: Element,
    value: String,
    isLast: Boolean,
//    var isGrafted: Boolean,
) : Property(element, value, isLast)
