package mock

import mock.property.Property
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import mock.property.PrimitiveProperty
import mock.tree.Tree
import mock.util.*
import mock.util.isClass
import mock.util.isEnum
import mock.util.isSealed
import java.io.File
import java.util.*
import javax.lang.model.element.Element

/**
 * 代码生成器
 */
internal class SourceFileGenerator(
    private val elementEnumSet: Set<Element>,
    private val elementSealedSet: Set<Element>,
    private val generateFilePath: String,
    private val generateFilePackageName: String,
    private val fileNameType: String
) {

    private val dir = File(generateFilePath)

    fun generateSourceFiles(treeList: List<Tree>) {
        treeList.forEach {
            generateSourceFile(it)
        }
    }

    fun generateSourceFile(caseTree: Tree) {
        // 写出 kt文件名
        val file = FileSpec.builder(
            generateFilePackageName,
            "${caseTree.property.value}${fileNameType}",
        )

        file.generateVal(caseTree)
        file.generateList(caseTree)

        // 生成目录；用于写入
        dir.mkdirs()
        file.build().writeTo(dir)
    }

    private fun FileSpec.Builder.generateVal(caseTree: Tree) {
        caseTree.startTraverseCompletePath().forEachIndexed { index, todoPath ->
            addPropertyWhenTodo(caseTree, todoPath, index)
        }
    }

    private fun FileSpec.Builder.addPropertyWhenTodo(
        caseTree: Tree,
        todoPath: List<Property>,
        index: Int
    ) {
        val codeBlock = generateCodeBlockStack(caseTree, todoPath)
        val property = PropertySpec.builder(
            "${caseTree.property.value.toLowerCaseInFirst()}_$index",
            ClassName.bestGuess(caseTree.property.element.asType().toString())
        )
            .addKdoc("${caseTree.property.element.asType()}_$index")
            // 初始化值
            .initializer(codeBlock)
            .build()
        addProperty(property)
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

    private fun FileSpec.Builder.generateList(caseTree: Tree) {
        val codeBlock = CodeBlock.builder()
            .add("listOf(")
            .apply {
                caseTree.startTraverseCompletePath().forEachIndexed { index, list ->
                    add("${caseTree.property.value.toLowerCaseInFirst()}_$index, ")
                }
            }
            .add(")")
            .build()
        val property = PropertySpec.builder(
            "${caseTree.property.value.toLowerCaseInFirst()}_List",
            ClassName("kotlin.collections", "List")
                .parameterizedBy(ClassName.bestGuess(caseTree.property.element.asType().toString())),
        )
            .addKdoc("${caseTree.property.element.asType()}_List")
            // 初始化值
            .initializer(codeBlock)
            .build()
        addProperty(property)
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
            if (!property.isLast) {
                when {
                    property.element.asType().isEnum(elementEnumSet) -> {
                        builder.add("\n$property,")
                    }
                    property.element.asType().isSealed(elementSealedSet) -> {
                        builder.add("\n$property,")
                    }
                    property.element.asType().isList() -> {
                        builder.add("\n$property,")
                    }
                    (property as? PrimitiveProperty)?.needMock == true -> {
                        builder.add("\n$property,")
                    }
                    property.element.asType().isClass() -> {
                        builder.add("\n$property(")
                        stack.push(property)
                    }
                    else -> {
                        builder.add("\n$property,")
                    }
                }
            } else {
                builder.add("\n$property,\n),")
                stack.pop()
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
