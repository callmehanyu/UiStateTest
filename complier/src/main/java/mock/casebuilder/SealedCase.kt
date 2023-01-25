package mock.casebuilder

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

//        messager.printMessage(Diagnostic.Kind.NOTE, "sealed=${sealedFather.enclosedElements.filter { it.kind == ElementKind.FIELD }.map { it.simpleName }}")

        val sealedFatherFieldList = sealedFather.enclosedElements
            .filter { it.kind == ElementKind.FIELD }
            .map { "${it.simpleName} = $TODO_STRING, " }

//        messager.printMessage(Diagnostic.Kind.NOTE, "fieldList=${sealedFatherFieldList}")

        return sealedFather.enclosedElements
            ?.asSequence()
            ?.filter { it.kind == ElementKind.CLASS }
            ?.map { sealedChild ->
//                messager.printMessage(Diagnostic.Kind.NOTE, "sealedChild=${sealedChild.enclosedElements.map { it.simpleName }}")

                val sealedChildFieldList = sealedChild.enclosedElements
                    .filter { it.kind == ElementKind.FIELD }
                    .map { "${it.simpleName} = $TODO_STRING, " }

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

//        messager.printMessage(Diagnostic.Kind.NOTE, "sealed=${sealedFather.enclosedElements.filter { it.kind == ElementKind.FIELD }.map { it.simpleName }}")

        val sealedFatherFieldList = sealedFather.enclosedElements
            .filter { it.kind == ElementKind.FIELD }
            .map { "${it.simpleName} = $TODO_STRING, " }

//        messager.printMessage(Diagnostic.Kind.NOTE, "fieldList=${sealedFatherFieldList}")

        return sealedFather.enclosedElements
            ?.asSequence()
            ?.filter { it.kind == ElementKind.CLASS }
            ?.map { sealedChild ->
//                messager.printMessage(Diagnostic.Kind.NOTE, "sealedChild=${sealedChild.enclosedElements.map { it.simpleName }}")

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