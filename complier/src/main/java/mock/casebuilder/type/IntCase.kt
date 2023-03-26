package mock.casebuilder.type

import mock.casebuilder.Case
import mock.property.PrimitiveProperty
import mock.tree.Tree
import javax.lang.model.element.Element

internal class IntCase(
    private val intDef: LongArray,
) : Case {
    override fun build(element: Element, isLast: Boolean): List<Tree> {
        return intDef.map { Tree(PrimitiveProperty(element, it.toString(), isLast)) }
    }
}