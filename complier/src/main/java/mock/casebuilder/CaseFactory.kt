@file:Suppress("JAVA_MODULE_DOES_NOT_EXPORT_PACKAGE")
package mock.casebuilder

import com.mock.annotation.custom.UiStateTestCustomDeclared
import com.mock.annotation.custom.UiStateTestCustomInt
import com.mock.annotation.custom.UiStateTestCustomString
import com.mock.annotation.unique.UiStateTestUnique
import com.sun.tools.javac.code.Symbol
import mock.casebuilder.type.*
import mock.casebuilder.type.BooleanCase
import mock.casebuilder.type.DeclaredCase
import mock.casebuilder.type.IntCase
import mock.casebuilder.type.SealedCase
import mock.casebuilder.type.StringCase
import mock.messager
import mock.property.PrimitiveProperty
import mock.util.*
import mock.util.isEnum
import mock.util.isSealed
import mock.util.isString
import mock.tree.Tree
import mock.tree.buildTree
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind
import javax.tools.Diagnostic


internal const val TODO_STRING = "TODO()"

/**
 * 生成完备的测试用例
 * bool enum intdef stringdef sealed内部
 * todo sealed外部 暂不支持
 * todo 改名怎么办？
 */
internal class CaseFactory(
    private val elementEnumSet: Set<Element>,
    private val elementSealedSet: Set<Element>,
    private val elementsDeclaredSet: Set<Element>,
    private val declaredCaseTreeList: List<Tree>,
    private val generateFilePath: String,
    private val generateFilePackageName: String,
) {

    fun buildCompleteCases(tree: Tree) {
        val list = tree.getPropertyList()
        list.forEach { enclosedElement ->
            buildCasesPerTree(enclosedElement, tree, false)
        }
    }

    fun buildCompleteCasesWhenDeclared(tree: Tree) {
        val list = tree.getPropertyList()
        list.forEachIndexed { index, enclosedElement ->
            val isLast = index == list.lastIndex
            buildCasesPerTree(enclosedElement, tree, isLast)
        }
    }

    private fun buildCasesPerTree(enclosedElement: Element, tree: Tree, isLast: Boolean) {
        enclosedElement.getAnnotation(UiStateTestUnique::class.java)?.let {
            buildCasesWhenUnique(enclosedElement, tree, it, isLast)
        }
        enclosedElement.getAnnotation(UiStateTestCustomString::class.java)?.let {
            buildCasesWhenCustomString(enclosedElement, tree, it.customString, isLast)
        }
        enclosedElement.getAnnotation(UiStateTestCustomInt::class.java)?.let {
            buildCasesWhenCustomInt(enclosedElement, tree, it.customInt, isLast)
        }
        enclosedElement.getAnnotation(UiStateTestCustomDeclared::class.java)?.let {
            buildCasesWhenCustomDeclared(enclosedElement, tree, it.instanceToString, isLast)
        }
    }

    private fun buildCasesWhenUnique(
        element: Element,
        treeRoot: Tree,
        annotation: UiStateTestUnique,
        isLast: Boolean
    ) {

        val type = element.asType()
        val cases = when {
            type.isBoolean() -> {
                BooleanCase().build(element, isLast)
            }
            type.isInt() -> {
                IntCase(annotation.intDef).build(element, isLast)
            }
            type.isString() -> {
                StringCase(annotation.stringDef).build(element, isLast)
            }
            type.isEnum(elementEnumSet) -> { //先处理 enum，再处理 isClass
                EnumCase(elementEnumSet).build(element, isLast)
            }
            type.isSealed(elementSealedSet) -> {
                SealedCase(elementSealedSet).build(element, isLast)
            }
            type.isList() -> {
                buildListCase(element, isLast, annotation.classDef)
            }
            type.isClass() -> {
                DeclaredCase().build(element, isLast)
            }
            else -> {
                messager.printMessage(Diagnostic.Kind.WARNING, "不支持该类型 ${element.simpleName}")
                emptyList()
            }
        }

        buildTree(treeRoot, cases)
    }

    private fun buildListCase(
        element: Element,
        isLast: Boolean,
        classDef: Array<String>,
    ): List<Tree> {
        /**
         * 获取泛型的element
         */
        val hasUiStateTestDeclaredAnnotation = elementsDeclaredSet
            .any { it.asType().toString() == element.asType().getGenerics() }

        return if (hasUiStateTestDeclaredAnnotation) {
            ListDeclaredCase(
                elementEnumSet,
                elementSealedSet,
                elementsDeclaredSet,
                declaredCaseTreeList,
                generateFilePath,
                generateFilePackageName,
            ).build(element, isLast)
        } else {
            ListNativeCase(classDef).build(element, isLast)
        }
    }

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
    private fun buildCasesWhenCustomInt(element: Element, treeRoot: Tree, customInt: Long, isLast: Boolean) {
        val property = PrimitiveProperty(element, customInt.toString(), isLast)
        val tree = Tree(property)
        val case = listOf(tree)
        buildTree(treeRoot, case)
    }

    /**
     *
     */
    private fun buildCasesWhenCustomDeclared(element: Element, treeRoot: Tree, instanceToString: String, isLast: Boolean) {
        val property = PrimitiveProperty(element, instanceToString, isLast, true)
        val tree = Tree(property)
        val case = listOf(tree)
        buildTree(treeRoot, case)
    }

}

/**
 * 过滤掉 static 属性的原因：
 * @Parcelize 注解会在类中生成 public static final Parcelable.Creator CREATOR = new Creator();
 * 该属性不参与测试用例生成，所以通过判断 isStatic 过滤
 */
private fun Tree.getPropertyList(): List<Element> {
    return property.element.enclosedElements.asSequence()
        .filter { it.kind == ElementKind.FIELD }
        .filterNot {
            (it as Symbol.VarSymbol).isStatic || anyOfUiStateTest(it)
        }
        .toList()
}

private fun anyOfUiStateTest(element: Element): Boolean {
    return element.getAnnotation(UiStateTestUnique::class.java) == null
            && element.getAnnotation(UiStateTestCustomDeclared::class.java) == null
            && element.getAnnotation(UiStateTestCustomInt::class.java) == null
            && element.getAnnotation(UiStateTestCustomString::class.java) == null
}
