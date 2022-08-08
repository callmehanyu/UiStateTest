package com.gray.annotation_demo


data class Property(
    val name: String,
    val type: String,
    val value: String,
) {

    override fun toString(): String {
        return "$name = $value, "
    }
}

class Tree(
    val property: Property,
    val children: MutableList<Tree> = mutableListOf(),
) {

    fun traverseCompletePath(): List<List<Property>> {
        val result = mutableListOf<List<Property>>()
        traverseCompletePathByRecursion(this, mutableListOf(), result)
        return result
    }

    private fun traverseCompletePathByRecursion(root: Tree, path: MutableList<Property>, pathList: MutableList<List<Property>>) {

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
            traverseCompletePathByRecursion(it, tempPath, pathList)
        }
    }

//    fun printAllString(): List<String> {
//        val result = mutableListOf<String>()
//        listPath(this, "", result)
//        return result
//    }
//
//    private fun listPath(root: Tree, path: String, pathList: MutableList<String>) {
//        var path = path
//        if (root.children.isEmpty()) { //叶子节点
//            path += root.property.toString()
//            pathList.add(path) //将结果保存在list中
//            return
//        }
//
//        //非叶子节点
//        path = path + root.property.toString() + "->"
//
//        //进行子节点的递归
//        root.children.forEach {
//            listPath(it, path, pathList)
//        }
//    }

    /**
     * 深复制
     */
    fun copy(): Tree {
        val copiedRoot = Tree(property)
        copyByRecursion(copiedRoot)
        return copiedRoot
    }

    private fun copyByRecursion(copiedTree: Tree) {
        if (children.isEmpty()) {
            return
        }

        val copiedChildren = children.map { Tree(it.property) }

        copiedTree.children.addAll(copiedChildren)
        children.forEach { child ->
            copyByRecursion(child)
        }
    }

}

/**
 * 每次插入一个节点
 */
fun buildTree(tree: Tree, cases: List<Tree>) {
    if (tree.children.isEmpty()) {
        // map 解决 引用问题
        tree.children.addAll(cases.map { Tree(it.property) })
        return
    }

    tree.children.forEach {
        buildTree(it, cases)
    }
}


