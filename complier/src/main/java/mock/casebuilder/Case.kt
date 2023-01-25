package mock.casebuilder

import mock.tree.Tree
import javax.lang.model.element.Element

internal interface Case {

    fun build(element: Element, isLast: Boolean): List<Tree>

}