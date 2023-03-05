package mock.casebuilder.type

import mock.casebuilder.Case
import mock.property.PrimitiveProperty
import mock.tree.Tree
import javax.lang.model.element.Element

internal class BooleanCase : Case {
    override fun build(element: Element, isLast: Boolean): List<Tree> {
        val trueCase = Tree(PrimitiveProperty(element, "true", isLast))
        val falseCase = Tree(PrimitiveProperty(element, "false", isLast))
        return listOf(trueCase, falseCase)
    }
}