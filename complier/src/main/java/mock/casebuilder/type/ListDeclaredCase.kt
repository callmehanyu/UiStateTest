package mock.casebuilder.type

import com.mock.annotation.UiStateTestDeclared
import mock.SourceFileGenerator
import mock.casebuilder.Case
import mock.casebuilder.CaseFactory
import mock.tree.TreePruner
import mock.tree.graftTree
import mock.property.PrimitiveProperty
import mock.tree.Tree
import mock.util.getGenerics
import mock.util.toLowerCaseInFirst
import javax.lang.model.element.Element

/**
 * 获取List类所有case：泛型是带 [UiStateTestDeclared] 注解
 */
internal class ListDeclaredCase(
    private val elementEnumSet: Set<Element>,
    private val elementSealedSet: Set<Element>,
    private val elementsDeclaredSet: Set<Element>,
    private val declaredCaseTreeList: List<Tree>,
    private val generateFilePath: String,
    private val generateFilePackageName: String,
    ) : Case {

    override fun build(element: Element, isLast: Boolean): List<Tree> {

        /**
         * 获取泛型的element
         */
        val genericsElement = elementsDeclaredSet
            .find { it.asType().toString() == element.asType().getGenerics() }
            ?: return emptyList()

        /**
         * 1.建树
         */
        val root = Tree(PrimitiveProperty(genericsElement, genericsElement.simpleName.toString(), isLast))
        CaseFactory(
            elementEnumSet,
            elementSealedSet,
            elementsDeclaredSet,
            declaredCaseTreeList,
            generateFilePath,
            generateFilePackageName,
        ).buildCompleteCases(root)

        /**
         * 2.对于有嵌套关系的树，嫁接在一起
         */
        graftTree(root, declaredCaseTreeList)
        TreePruner(elementEnumSet, elementSealedSet).start(root)

        /**
         * 4.生成代码
         */
        SourceFileGenerator(
            elementEnumSet,
            elementSealedSet,
            generateFilePath,
            generateFilePackageName,
            "List"
        ).generateSourceFile(root)

        val value = "${root.property.value.toLowerCaseInFirst()}_List"
        return listOf(Tree(PrimitiveProperty(element, value, isLast)))
    }

}