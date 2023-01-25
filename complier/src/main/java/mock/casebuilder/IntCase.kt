package mock.casebuilder

import mock.property.PrimitiveProperty
import mock.tree.Tree
import javax.lang.model.element.Element

internal class IntCase(
    private val intDef: IntArray,
) : Case {
    override fun build(element: Element, isLast: Boolean): List<Tree> {
        return intDef.map { Tree(PrimitiveProperty(element, it.toString(), isLast)) }
    }
}