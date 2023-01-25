package mock.casebuilder

import mock.property.PrimitiveProperty
import mock.tree.Tree
import mock.util.findEnum
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind

internal class EnumCase(
    private val elementEnumSet: Set<Element>,
) : Case {
    override fun build(element: Element, isLast: Boolean): List<Tree> {
        val enum = element.asType().findEnum(elementEnumSet) ?: return emptyList()
        return enum.enclosedElements
            ?.asSequence()
            ?.filter { it.kind == ElementKind.ENUM_CONSTANT }
            ?.map { constantElement ->
                Tree(
                    PrimitiveProperty(
                        element,
                        "${element.asType()}.${constantElement.simpleName}",
                        isLast
                    )
                )
            }?.toList() ?: emptyList()
    }
}