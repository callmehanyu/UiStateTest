package com.mock

import com.mock.annotation.custom.UiStateTestCustomInt
import com.mock.annotation.custom.UiStateTestCustomString
import com.mock.annotation.unique.UiStateTestUnique
import com.mock.util.*
import com.mock.util.isEnum
import com.mock.util.isSealed
import com.mock.util.isString
import com.mock.vo.*
import javax.annotation.processing.Messager
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind
import javax.lang.model.type.TypeKind
import javax.tools.Diagnostic


internal const val TODO = "TODO()"
//internal const val TODO = "com.zhy.collection.util.TODOException()"

/**
 * 生成完备的测试用例
 * bool enum intdef stringdef sealed内部
 * todo list map
 * todo sealed外部 暂不支持
 * todo 改名怎么办？
 */
internal class CaseBuilder(
    private val elementEnumSet: Set<Element>,
    private val elementSealedSet: Set<Element>,
    private val elementsDeclaredSet: Set<Element>,
    private val messager: Messager
) {

    fun buildCompleteCases(tree: Tree) {
        val list = tree.property.element.enclosedElements
            .filter { it.kind == ElementKind.FIELD }
        list.forEach { enclosedElement ->
            enclosedElement.getAnnotation(UiStateTestUnique::class.java)?.let {
                buildCasesWhenUnique(enclosedElement, tree, it, false)
            }
            enclosedElement.getAnnotation(UiStateTestCustomString::class.java)?.let {
                buildCasesWhenCustomString(enclosedElement, tree, it.customString, false)
            }
            enclosedElement.getAnnotation(UiStateTestCustomInt::class.java)?.let {
                buildCasesWhenCustomInt(enclosedElement, tree, it.customInt, false)
            }
        }
//        messager.printMessage(Diagnostic.Kind.NOTE, treeRoot.printAllString().joinToString(";"))
    }

    fun buildCompleteCasesDeclared(tree: Tree) {
        val list = tree.property.element.enclosedElements
            .filter { it.kind == ElementKind.FIELD }
        list.forEachIndexed { index, enclosedElement ->

            val isLast = index == list.lastIndex

            val annotation = enclosedElement.getAnnotation(UiStateTestUnique::class.java)
            if (annotation == null) {
                buildCasesWithoutAnnotation(tree, enclosedElement, isLast)
            } else {
                buildCasesWhenUnique(enclosedElement, tree, annotation, isLast)
            }
        }
//        messager.printMessage(Diagnostic.Kind.NOTE, treeRoot.printAllString().joinToString(";"))
    }

    /**
     *
     */
    private fun buildCasesWithoutAnnotation(treeRoot: Tree, element: Element, isLast: Boolean) {
        val property = PrimitiveProperty(element, TODO, isLast)
        val tree = Tree(property)
        val case = listOf(tree)
        buildTree(treeRoot, case)
    }

    private fun buildCasesWhenUnique(
        element: Element,
        treeRoot: Tree,
        annotation: UiStateTestUnique,
        isLast: Boolean
    ) {

        val type = element.asType()

//        messager.printMessage(Diagnostic.Kind.NOTE, "type=${type.toString().split("<").joinToString(",")}")

        val cases = when {
            type.kind == TypeKind.BOOLEAN -> {
                getBooleanCases(element, isLast)
            }
            type.kind in typeKindInt -> {
                getIntCases(element, annotation.intDef, isLast)
            }
            type.isString() -> {
                getStringCases(element, annotation.stringDef, isLast)
            }
            type.isEnum(elementEnumSet) -> { //先处理 enum，再处理 isClass
                getEnumCases(element, isLast)
            }
            type.isSealed(elementSealedSet) -> {
                getSealedCases(element, isLast)
            }
            type.isList() -> {
                getListCases(element, isLast)
            }
            type.isClass() -> {
                getDeclaredCases(element, isLast)
            }
            else -> {
                messager.printMessage(Diagnostic.Kind.WARNING, "不支持该类型 ${element.simpleName}")
                emptyList()
            }
        }

        buildTree(treeRoot, cases)
    }

    private fun getBooleanCases(element: Element, isLast: Boolean): List<Tree> {
        val trueCase = Tree(PrimitiveProperty(element, "true", isLast))
        val falseCase = Tree(PrimitiveProperty(element, "false", isLast))
        return listOf(trueCase, falseCase)
    }

    private fun getIntCases(element: Element, intDef: IntArray, isLast: Boolean): List<Tree> =
        intDef.map { Tree(PrimitiveProperty(element, it.toString(), isLast)) }

    private fun getStringCases(
        element: Element,
        stringDef: Array<String>,
        isLast: Boolean
    ): List<Tree> = stringDef
        .map { Tree(PrimitiveProperty(element, "\"$it\"", isLast)) }

    private fun getEnumCases(element: Element, isLast: Boolean): List<Tree> {
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

    /**
     * 方案1：只有类名
     */
    private fun getSealedCases(element: Element, isLast: Boolean): List<Tree> {
        val sealedFather = element.asType().findSealed(elementSealedSet) ?: return emptyList()

//        messager.printMessage(Diagnostic.Kind.NOTE, "sealed=${sealedFather.enclosedElements.filter { it.kind == ElementKind.FIELD }.map { it.simpleName }}")

        val sealedFatherFieldList = sealedFather.enclosedElements
            .filter { it.kind == ElementKind.FIELD }
            .map { "${it.simpleName} = ${TODO}, " }

//        messager.printMessage(Diagnostic.Kind.NOTE, "fieldList=${sealedFatherFieldList}")

        return sealedFather.enclosedElements
            ?.asSequence()
            ?.filter { it.kind == ElementKind.CLASS }
            ?.map { sealedChild ->
//                messager.printMessage(Diagnostic.Kind.NOTE, "sealedChild=${sealedChild.enclosedElements.map { it.simpleName }}")

                val sealedChildFieldList = sealedChild.enclosedElements
                    .filter { it.kind == ElementKind.FIELD }
                    .map { "${it.simpleName} = ${TODO}, " }

                Tree(
                    PrimitiveProperty(
                        element,
                        "${element.asType()}.${sealedChild.simpleName}(${TODO})",
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
            .map { "${it.simpleName} = ${TODO}, " }

//        messager.printMessage(Diagnostic.Kind.NOTE, "fieldList=${sealedFatherFieldList}")

        return sealedFather.enclosedElements
            ?.asSequence()
            ?.filter { it.kind == ElementKind.CLASS }
            ?.map { sealedChild ->
//                messager.printMessage(Diagnostic.Kind.NOTE, "sealedChild=${sealedChild.enclosedElements.map { it.simpleName }}")

                val sealedChildFieldList = sealedChild.enclosedElements
                    .filter { it.kind == ElementKind.FIELD }
                    .map { "${it.simpleName} = ${TODO}, " }

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

    /**
     * 获取List类所有case
     * todo 未完成
     */
    private fun getListCases(element: Element, isLast: Boolean): List<Tree> {

//        messager.printMessage(Diagnostic.Kind.NOTE, "type=${element.asType().getGenerics()}")

        val generics = element.asType().getGenerics()
        if (generics?.isPrimitiveType() == true) {
//            messager.printMessage(Diagnostic.Kind.NOTE, "getGenerics()?.isPrimitiveType")
            val property = PrimitiveProperty(element, TODO, isLast)
            val tree = Tree(property)
            val case = listOf(tree)
            return case
        }

//        messager.printMessage(Diagnostic.Kind.NOTE, "generics=${generics}")
//        messager.printMessage(Diagnostic.Kind.NOTE, "elementsDeclaredSet=${elementsDeclaredSet.joinToString(";")}")
        if (elementsDeclaredSet.find { it.asType().toString() == generics } == null) {
//            messager.printMessage(Diagnostic.Kind.NOTE, "elementsDeclaredSet.find")
            val property = PrimitiveProperty(element, TODO, isLast)
            val tree = Tree(property)
            val case = listOf(tree)
            return case
        }

        return listOf(
            Tree(
                DeclaredProperty(
                    element,
                    "listOf()",
                    isLast = isLast
                )
            )
        )
    }

    /**
     * 获取自定义类所有case
     */
    private fun getDeclaredCases(element: Element, isLast: Boolean): List<Tree> =
        listOf(
            Tree(
                DeclaredProperty(
                    element,
                    element.asType().toString().toLowerCaseInFirst(),
//                    isGrafted = false,
                    isLast = isLast
                )
            )
        )

    /**
     *
     */
    private fun buildCasesWhenCustomString(element: Element, treeRoot: Tree, customString: String, isLast: Boolean) {
        val property = PrimitiveProperty(element, "\"${customString}\"", isLast)
        val tree = Tree(property)
        val case = listOf(tree)
        buildTree(treeRoot, case)
    }

    /**
     *
     */
    private fun buildCasesWhenCustomInt(element: Element, treeRoot: Tree, customInt: Int, isLast: Boolean) {
        val property = PrimitiveProperty(element, customInt.toString(), isLast)
        val tree = Tree(property)
        val case = listOf(tree)
        buildTree(treeRoot, case)
    }

}
