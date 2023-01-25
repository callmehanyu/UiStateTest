package mock.property

import javax.lang.model.element.Element

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
 */
internal class DeclaredProperty(
    element: Element,
    value: String,
    isLast: Boolean,
) : Property(element, value, isLast)
