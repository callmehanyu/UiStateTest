package mock.casebuilder

import mock.property.PrimitiveProperty
import mock.tree.Tree
import javax.lang.model.element.Element

internal class StringCase(
    private val stringDef: Array<String>
) : Case {
    override fun build(element: Element, isLast: Boolean): List<Tree> {
        return stringDef
            .map { Tree(PrimitiveProperty(element, "\"$it\"", isLast)) }
    }
}