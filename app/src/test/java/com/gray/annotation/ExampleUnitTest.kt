package com.gray.annotation

import android.util.Log
import com.gray.annotation_demo.Property
import com.gray.annotation_demo.Tree
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        val property = Property(
            "typeElement",
            "Type", "null"
        )
        val treeRoot = Tree(property)
        repeat(3) {
            traverseTree(getBooleanCases(), treeRoot)
        }
//        Log.d("zhy", treeRoot.printAll().joinToString(";"))
    }

    private fun getBooleanCases(): List<Tree> {
        val trueCase = Tree(
            Property(
                "element",
                "Type",
                "true"
            )
        )
        val falseCase = Tree(
            Property(
                "element",
                "Type",
                "false"
            )
        )
        return listOf(trueCase, falseCase)
    }

    private fun traverseTree(cases: List<Tree>, tree: Tree) {
        if (tree.children.isEmpty()) {
            tree.children.addAll(cases)
            return
        }
        tree.children.forEach {
            traverseTree(cases, it)
        }
    }

    @Test
    fun up() {
        "boolean".toUpperCaseInFirst()
    }

    fun String.toUpperCaseInFirst(): String = this.mapIndexed { index, c ->
        if (index == 0) {
            c.toUpperCase()
        } else {
            c
        }
    }.joinToString("")
}