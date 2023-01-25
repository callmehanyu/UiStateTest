package mock.tree

import mock.property.Property


internal class Tree(
    val property: Property,
    val children: MutableList<Tree> = mutableListOf(),
) {

    fun startTraverseCompletePath(): List<List<Property>> {
        val result = mutableListOf<List<Property>>()
        traverseCompletePath(this, mutableListOf(), result)
        return result
    }

    private fun traverseCompletePath(root: Tree, path: MutableList<Property>, pathList: MutableList<List<Property>>) {

        val tempPath = mutableListOf<Property>()
            .apply {
                addAll(path)
                add(root.property)
            }

        if (root.children.isEmpty()) { //叶子节点
            pathList.add(tempPath) //将结果保存在list中
            return
        }

        //进行子节点的递归
        root.children.forEach {
            traverseCompletePath(it, tempPath, pathList)
        }
    }


    fun printAllString(): List<String> {
        val result = mutableListOf<String>()
        listPath(this, "", result)
        return result
    }

    private fun listPath(root: Tree, path: String, pathList: MutableList<String>) {
        var path = path
        if (root.children.isEmpty()) { //叶子节点
            path += root.property.toLogString()
            pathList.add(path) //将结果保存在list中
            return
        }

        //非叶子节点
        path = path + root.property.toLogString() + "->"

        //进行子节点的递归
        root.children.forEach {
            listPath(it, path, pathList)
        }
    }

}

/**
 * 每次插入一个节点
 */
internal fun buildTree(tree: Tree, cases: List<Tree>) {
    if (tree.children.isEmpty()) {
        // map 解决 引用问题
        tree.children.addAll(cases.map { Tree(it.property) })
        return
    }

    tree.children.forEach {
        buildTree(it, cases)
    }
}

/**
 * 把 tree 复制到 copiedTree
 */
internal fun copy(tree: Tree, copiedTree: Tree) {
    if (tree.children.isEmpty()) {
        return
    }

    val copiedChildren = tree.children.map { Tree(it.property) }
    copiedTree.children.addAll(copiedChildren)

    tree.children.forEachIndexed { index, child ->
        copy(child, copiedTree.children[index])
    }
}

/**
 * 在一棵树的每个叶子节点位置，插入一颗完整的树
 */
internal fun graftTreeWhenLeaves(root: Tree, branch: Tree) {
    if (root.children.isEmpty()) {
        graftTreeChildren(root, branch)
        return
    }

    root.children.forEach {
        graftTreeWhenLeaves(it, branch)
    }
}

private fun graftTreeChildren(root: Tree, branch: Tree) {
    val rootCopied = Tree(root.property).apply {
        copy(branch, this)
    }
    root.children.addAll(rootCopied.children)
}






