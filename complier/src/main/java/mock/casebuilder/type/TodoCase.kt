package mock.casebuilder.type

import mock.casebuilder.Case
import mock.casebuilder.TODO_STRING
import mock.property.PrimitiveProperty
import mock.tree.Tree
import javax.lang.model.element.Element

internal class TodoCase : Case {

    override fun build(element: Element, isLast: Boolean): List<Tree> {

        val property = PrimitiveProperty(element, TODO_STRING, isLast)
        val tree = Tree(property)
        val case = listOf(tree)

        return case
    }
}