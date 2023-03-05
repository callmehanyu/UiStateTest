package mock.casebuilder.type

import mock.casebuilder.Case
import mock.casebuilder.TODO_STRING
import mock.property.PrimitiveProperty
import mock.tree.Tree
import mock.util.findSealed
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind

internal class SealedCase(
    private val elementSealedSet: Set<Element>,
) : Case {
    override fun build(element: Element, isLast: Boolean): List<Tree> {
        return getSealedCases(element, isLast)
    }

    /**
     * 方案1：只有类名
     */
    private fun getSealedCases(element: Element, isLast: Boolean): List<Tree> {
        val sealedFather = element.asType().findSealed(elementSealedSet) ?: return emptyList()

        return sealedFather.enclosedElements
            ?.asSequence()
            ?.filter { it.kind == ElementKind.CLASS }
            ?.map { sealedChild ->

                Tree(
                    PrimitiveProperty(
                        element,
                        "${element.asType()}.${sealedChild.simpleName}($TODO_STRING)",
                        isLast
                    )
                )
            }?.toList()
            ?: emptyList()
    }

    /**
     * 方案2：包括属性
     */
    private fun getSealedCasesDetail(element: Element, isLast: Boolean): List<Tree> {
        val sealedFather = element.asType().findSealed(elementSealedSet) ?: return emptyList()

        val sealedFatherFieldList = sealedFather.enclosedElements
            .filter { it.kind == ElementKind.FIELD }
            .map { "${it.simpleName} = $TODO_STRING, " }

        return sealedFather.enclosedElements
            ?.asSequence()
            ?.filter { it.kind == ElementKind.CLASS }
            ?.map { sealedChild ->

                val sealedChildFieldList = sealedChild.enclosedElements
                    .filter { it.kind == ElementKind.FIELD }
                    .map { "${it.simpleName} = $TODO_STRING, " }

                Tree(
                    PrimitiveProperty(
                        element,
                        "${element.asType()}.${sealedChild.simpleName}(" +
                                sealedFatherFieldList.joinToString("") +
                                sealedChildFieldList.joinToString("") +
                                ")",
                        isLast
                    )
                )
            }?.toList()
            ?: emptyList()
    }
}