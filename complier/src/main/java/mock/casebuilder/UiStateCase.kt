package mock.casebuilder

import mock.tree.TreePruner
import com.mock.annotation.UiStateTest
import mock.tree.graftTree
import mock.property.PrimitiveProperty
import mock.tree.Tree
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement

/**
 * 获取UiState类所有case
 * 起点
 */
internal class UiStateCase(
    private val roundEnv: RoundEnvironment,
    private val elementEnumSet: Set<Element>,
    private val elementSealedSet: Set<Element>,
    private val declaredCaseTreeList: List<Tree>,
    private val generateFilePath: String,
    private val generateFilePackageName: String,
) {

    private val caseBuilder by lazy {
        CaseFactory(
            elementEnumSet,
            elementSealedSet,
            declaredCaseTreeList,
            generateFilePath,
            generateFilePackageName,
        )
    }

    fun build(): List<Tree> {

        /**
         * 1.对每个 UiState，建树
         */
        val caseTreeList = buildUiStateTestTreeList(roundEnv)
        if (caseTreeList.isEmpty()) {
            return emptyList()
        }

        /**
         * 3.对于有嵌套关系的树，嫁接在一起
         */
        val treePruner = TreePruner(elementEnumSet, elementSealedSet)

        caseTreeList.forEach { case ->
            graftTree(case, declaredCaseTreeList)
            treePruner.start(case)
        }

        return caseTreeList

    }

    /**
     *
     */
    private fun buildUiStateTestTreeList(roundEnv: RoundEnvironment): List<Tree> =
        roundEnv.getElementsAnnotatedWith(UiStateTest::class.java)
            .asSequence()
            .filter { it.kind.isClass && it is TypeElement }
            .map {
                val root = Tree(PrimitiveProperty(it, it.simpleName.toString(), isLast = true))
                caseBuilder.buildCompleteCases(root)
                root
            }
            .toList()

}