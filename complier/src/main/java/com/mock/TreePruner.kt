package com.mock

import com.mock.annotation.limit.*
import com.mock.annotation.limit.Boolean
import com.mock.annotation.limit.Enum
import com.mock.util.*
import com.mock.vo.Tree
import javax.annotation.processing.Messager
import javax.lang.model.element.Element
import javax.lang.model.type.MirroredTypeException
import javax.lang.model.type.TypeKind
import javax.lang.model.type.TypeMirror
import javax.tools.Diagnostic

/**
 * 裁剪树
 */
internal class TreePruner(
    private val elementEnumSet: Set<Element>,
    private val elementSealedSet: Set<Element>,
    private val messager: Messager,
) {

    /**
     * key:limitId,
     * value:限制路径上，最后一个节点的引用
     */
    private val limitMap by lazy { mutableMapOf<String, Tree>() }

    internal fun start(caseTree: Tree) {
        recordLimitTreeRecursively(caseTree)
//        messager.printMessage(Diagnostic.Kind.NOTE, "TreePruner:${limitMap.size}")
        pruneTree(caseTree, limitMap.values.toList())
    }

    private fun recordLimitTreeRecursively(tree: Tree) {
        recordLimitNode(tree)
        if (tree.children.isEmpty()) {
            return
        }
        tree.children.forEach {
            recordLimitTreeRecursively(it)
        }
    }

    private fun recordLimitNode(tree: Tree) {
        val type = tree.property.element.asType()
        when {
            type.kind == TypeKind.BOOLEAN -> {
//                messager.printMessage(Diagnostic.Kind.NOTE, "recordLimitNode ${tree.property.element}")
                recordLimitBooleanNode(tree)
            }
            type.kind in typeKindInt -> {
                recordLimitIntDefNode(tree)
            }
            type.isString() -> {
                recordLimitStringDefNode(tree)
            }
            type.isEnum(elementEnumSet) -> { //先处理 enum，再处理 isClass
                recordLimitEnumNode(tree)
            }
            type.isSealed(elementSealedSet) -> {
                recordLimitSealedNode(tree)
            }
            else -> {
                messager.printMessage(Diagnostic.Kind.WARNING, "recordLimitNode 不支持该类型 ${type}")
            }
        }
    }

    private fun recordLimitBooleanNode(tree: Tree) {
        val property = tree.property
        val annotation =
            property.element.getAnnotation(UiStateTestLimitBoolean::class.java) ?: return
//        messager.printMessage(Diagnostic.Kind.NOTE, " filter ${annotation.cases.size}")

        annotation.cases
            .filter { property.value == it.value.toString() }
            .forEach { cases: Boolean ->
//                messager.printMessage(Diagnostic.Kind.NOTE, "limitIdList ${cases.value}")
                cases.limitIdList.forEach { limitId ->
                    if (limitMap.contains(limitId) && limitMap[limitId]?.property.toString() == tree.property.toString()) {
                        return@forEach
                    }
//                    messager.printMessage(Diagnostic.Kind.NOTE, "recordLimitBooleanNode ${tree}")
                    limitMap[limitId] = tree
                }
            }
    }

    private fun recordLimitIntDefNode(tree: Tree) {
        val property = tree.property
        val annotation =
            property.element.getAnnotation(UiStateTestLimitIntDef::class.java) ?: return
//        messager.printMessage(Diagnostic.Kind.NOTE, " filter ${annotation.cases.size}")

        annotation.cases
            .filter { property.value == it.value.toString() }
            .forEach { cases: IntDef ->
                cases.limitIdList.forEach { limitId ->
                    if (limitMap.contains(limitId) && limitMap[limitId]?.property.toString() == tree.property.toString()) {
                        return@forEach
                    }
//                    messager.printMessage(Diagnostic.Kind.NOTE, "limitIdList ${cases.value}")
                    limitMap[limitId] = tree
                }
            }
    }

    private fun recordLimitStringDefNode(tree: Tree) {
        val property = tree.property
        val annotation =
            property.element.getAnnotation(UiStateTestLimitStringDef::class.java) ?: return
//        messager.printMessage(Diagnostic.Kind.NOTE, " filter ${annotation.cases.size}")

        annotation.cases
            .filter { property.value == "\"${it.value}\"" }
            .forEach { cases: StringDef ->
                cases.limitIdList.forEach { limitId ->
//                    messager.printMessage(Diagnostic.Kind.NOTE, "limitIdList ${cases.value}")

                    if (limitMap.contains(limitId) && limitMap[limitId]?.property.toString() == tree.property.toString()) {
                        return@forEach
                    }
//                    messager.printMessage(Diagnostic.Kind.NOTE, "limitIdList ${cases.value}")
                    limitMap[limitId] = tree
                }
            }
    }

    /**
     * todo
     */
    private fun recordLimitEnumNode(tree: Tree) {
        val property = tree.property
        val annotation = property.element.getAnnotation(UiStateTestLimitEnum::class.java) ?: return
        messager.printMessage(Diagnostic.Kind.NOTE, " property.value ${property.value}")

        annotation.cases
            .filter { cases: Enum ->
                messager.printMessage(
                    Diagnostic.Kind.NOTE,
                    "property.value=${property.value.lastName()}, cases.value= ${cases.value}"
                )

                property.value.lastName() == cases.value
            }
            .forEach { cases: Enum ->
                cases.limitIdList.forEach { limitId ->
//                    messager.printMessage(Diagnostic.Kind.NOTE, "limitIdList ${cases.value}")

                    if (limitMap.contains(limitId) && limitMap[limitId]?.property.toString() == tree.property.toString()) {
                        return@forEach
                    }
//                    messager.printMessage(Diagnostic.Kind.NOTE, "limitIdList ${cases.value}")
                    limitMap[limitId] = tree
                }
            }
    }

    private fun recordLimitSealedNode(tree: Tree) {
        val property = tree.property
        val annotation =
            property.element.getAnnotation(UiStateTestLimitSealed::class.java) ?: return
//        messager.printMessage(Diagnostic.Kind.NOTE, " property.value ${property.value}")

        annotation.cases
            .filter {
                var upgradeTypeMirror: TypeMirror? = null
                try {
                    it.value
                } catch (e: MirroredTypeException) {
                    upgradeTypeMirror = e.typeMirror
                }
//                messager.printMessage(Diagnostic.Kind.NOTE, "property.value=${property.value}, upgradeTypeMirror= ${upgradeTypeMirror}(${TODO})")
                property.value == "${upgradeTypeMirror}(${TODO})"
            }
            .forEach { cases: Sealed ->
                cases.limitIdList.forEach { limitId ->
//                    messager.printMessage(Diagnostic.Kind.NOTE, "limitIdList ${cases.value}")

                    if (limitMap.contains(limitId) && limitMap[limitId]?.property.toString() == tree.property.toString()) {
                        return@forEach
                    }
//                    messager.printMessage(Diagnostic.Kind.NOTE, "limitIdList ${cases.value}")
                    limitMap[limitId] = tree
                }
            }
    }

    private fun pruneTree(tree: Tree, limitTree: List<Tree>) {
        pruneTreeRecursively(tree, limitTree)
    }

    private fun pruneTreeRecursively(tree: Tree, limitTree: List<Tree>) {
        val children = tree.children
        if (children.isEmpty()) {
            return
        }
        var index = 0
        while (index < children.size) {
            val child = children[index]
            if (limitTree.contains(child)) {
                children.removeAt(index)
            } else {
                pruneTreeRecursively(child, limitTree)
                index++
            }
        }
    }

}
