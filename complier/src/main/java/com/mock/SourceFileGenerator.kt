package com.mock

import com.mock.util.isClass
import com.mock.util.isEnum
import com.mock.util.isSealed
import com.mock.util.toLowerCaseInFirst
import com.mock.vo.Property
import com.mock.vo.Tree
import com.squareup.kotlinpoet.*
import java.io.File
import java.util.*
import javax.annotation.processing.Messager
import javax.lang.model.element.Element
import javax.tools.Diagnostic

/**
 * 代码生成器
 */
internal class SourceFileGenerator(
    private val elementEnumSet: Set<Element>,
    private val elementSealedSet: Set<Element>,
    private val messager: Messager,
) {

    private val dir = File("UiTestMock")

    internal fun generateSourceFiles(treeList: List<Tree>) {

        dir.deleteRecursively()

        treeList.forEach {
//            messager.printMessage(Diagnostic.Kind.NOTE, "generateSourceFiles")
            generateSourceFile(it)
        }
    }

    private fun generateSourceFile(caseTree: Tree) {
        // 写出 kt文件名 todo package
        val file = FileSpec.builder("", "${caseTree.property.value}Complier")

//        messager.printMessage(Diagnostic.Kind.NOTE, "caseTree:"+caseTree.printAllString().joinToString(";"))

        caseTree.startTraverseCompletePath().forEachIndexed { index, propertyList ->

            val codeBlock = generateCodeBlockStack(caseTree, propertyList)

            val property = PropertySpec.builder(
                "${caseTree.property.value.toLowerCaseInFirst()}_$index",
                ClassName.bestGuess(caseTree.property.element.asType().toString())
            )
                .addKdoc("${caseTree.property.element.asType()}_$index")
                // 初始化值
                .initializer(codeBlock)
                .build()
            file.addProperty(property)
        }

        // 生成目录；用于写入
        dir.mkdirs()
        file.build().writeTo(dir)
    }

    private fun generateCodeBlockStack(caseTree: Tree, propertyList: List<Property>): CodeBlock {
        val stack = Stack<Property>()

        val codeBlock = CodeBlock.builder()
            .add("%T(", caseTree.property.element.asType())
            .apply {
                buildLeftParenthesis(this, propertyList, stack)
                buildRightParenthesis(this, stack)
            }
            .add("\n)")
            .build()
        return codeBlock
    }

    private fun buildLeftParenthesis(
        builder: CodeBlock.Builder,
        propertyList: List<Property>,
        stack: Stack<Property>
    ) {
        propertyList.forEachIndexed { index, property ->
            if (index == 0) {
                return@forEachIndexed
            }
            when {
                property.element.asType().isEnum(elementEnumSet) -> {
                    builder.add("\n$property,")
                }
                property.element.asType().isSealed(elementSealedSet) -> {
                    builder.add("\n$property,")
                }
                property.element.asType().isClass() -> {
                    if (property.value == TODO) {
                        builder.add("\n${property}")
                    } else {
                        builder.add("\n$property(")
                        stack.push(property)
                    }
                }
                property.isLast -> {
                    builder.add("\n$property,\n),")
                    stack.pop()
                }
                else -> {
                    builder.add("\n$property, ")
                }
            }
        }
    }

    private fun buildRightParenthesis(builder: CodeBlock.Builder, stack: Stack<Property>) {
        for (i in stack.indices) {
            builder.add("\n)")
            stack.pop()
        }
    }

}
