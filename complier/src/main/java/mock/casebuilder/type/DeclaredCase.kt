package mock.casebuilder.type

import mock.casebuilder.Case
import mock.property.DeclaredProperty
import mock.tree.Tree
import mock.util.toLowerCaseInFirst
import javax.lang.model.element.Element

internal class DeclaredCase : Case {
    override fun build(element: Element, isLast: Boolean): List<Tree> {
        return listOf(
            Tree(
                DeclaredProperty(
                    element,
                    element.asType().toString().toLowerCaseInFirst(),
                    isLast = isLast
                )
            )
        )
    }
}