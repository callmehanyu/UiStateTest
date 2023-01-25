package mock

import mock.casebuilder.TODO_STRING
import mock.property.Property
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
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
    private val fileNameType: String
) {

    private val dir = File("/Users/zhanghanyu01/Documents/GitHub/UiTest/app/src/main/java")
//    private val dir = File("/Users/zhanghanyu01/Desktop/temp3/baidu/youavideo/android/app/src/androidTest/java")
//    private val dir = File("/Users/zhanghanyu01/Desktop/temp3/baidu/youavideo/android/business_home/src/androidTest/java") // todo

    fun generateSourceFiles(treeList: List<Tree>) {

//        dir.deleteRecursively()

        treeList.forEach {
//            messager.printMessage(Diagnostic.Kind.NOTE, "generateSourceFiles")
            generateSourceFile(it)
        }
    }

    fun generateSourceFile(caseTree: Tree) {
        // 写出 kt文件名
        val file = FileSpec.builder("com.zhy.demo.mock", "${caseTree.property.value}${fileNameType}")

        file.generateVal(caseTree)
        file.generateList(caseTree)

        // 生成目录；用于写入
        dir.mkdirs()
        file.build().writeTo(dir)
    }

    private fun FileSpec.Builder.generateVal(caseTree: Tree) {
//                messager.printMessage(Diagnostic.Kind.NOTE, "caseTree:"+caseTree.printAllString().joinToString(";"))

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

//    private fun addPropertyWhenCollection(
//        caseTree: Tree,
//        index: Int,
//        file: FileSpec.Builder
//    ) {
//        val code = "${collectionAnnotation.className}(${collectionAnnotation.property})"
//        val codeBlock = CodeBlock.builder()
//            .add(code)
//            .build()
//        val property = PropertySpec.builder(
//            "${caseTree.property.value.toLowerCaseInFirst()}_$index",
//            ClassName.bestGuess(caseTree.property.element.asType().toString())
//        )
//            .addKdoc("${caseTree.property.element.asType()}_$index")
//            // 初始化值
//            .initializer(codeBlock)
//            .build()
//        file.addProperty(property)
//    }

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
                property.element.asType().isClass() -> {
                    if (property.value == TODO_STRING) {
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
