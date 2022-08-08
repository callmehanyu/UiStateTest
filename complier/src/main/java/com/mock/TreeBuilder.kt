package com.mock

import com.mock.vo.DeclaredProperty
import com.mock.vo.Tree
import com.mock.vo.copy
import com.mock.vo.graftTreeWhenLeaves

internal fun graftTree(caseTree: Tree, declaredCaseTreeList: List<Tree>) {

//        messager.printMessage(Diagnostic.Kind.NOTE, "graftTree=${caseTree.property.element}")
    graftTreeNode(caseTree, declaredCaseTreeList)

    if (caseTree.children.isEmpty()) {
        return
    }

    caseTree.children.forEach {
        graftTree(it, declaredCaseTreeList)
    }
}

private fun graftTreeNode(root: Tree, branches: List<Tree>) {
//        if (root.property !is DeclaredProperty || root.property.isGrafted) {
    if (root.property !is DeclaredProperty) {
//            messager.printMessage(Diagnostic.Kind.NOTE, "return=${root.property.toLogString()}")
        return
    }

    // 1.先把当前节点以及子节点 复制到 临时的一棵树 rootCopied
    val rootCopied = Tree(root.property).apply {
        copy(root, this)
    }
//        messager.printMessage(Diagnostic.Kind.NOTE, "graftTreeNode:"+rootCopied.printAllString().joinToString(";"))

    // 3.枚举当前节点的所有情况，加入子节点中
    val branch = branches
        .find { it.property.element.asType() == root.property.element.asType() } ?: return
//        messager.printMessage(Diagnostic.Kind.NOTE, "graftTreeNode root=${branch.children.size}")

    val caseRoot = Tree(root.property).apply {
        copy(branch, this)
    }
//        messager.printMessage(Diagnostic.Kind.NOTE, "graftTreeNode caseRoot=${caseRoot.children.size}")
    root.children.clear()
    root.children.addAll(caseRoot.children)
//        root.property.isGrafted = true

//        messager.printMessage(Diagnostic.Kind.NOTE, "changshiNode rootchild.children=${root.property.value}")

    //4.
    graftTreeWhenLeaves(root, rootCopied)
}