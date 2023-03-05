package mock.casebuilder.type

import com.mock.annotation.UiStateTestDeclared
import mock.casebuilder.Case
import mock.property.PrimitiveProperty
import mock.tree.Tree
import javax.lang.model.element.Element

/**
 * 获取List类case：泛型是不带 [UiStateTestDeclared] 注解
 */
internal class ListNativeCase(
	private val classDef: Array<String>
) : Case {
	override fun build(element: Element, isLast: Boolean): List<Tree> {
		return listOf(Tree(PrimitiveProperty(element, "listOf(${classDef.joinToString(",")})", isLast)))
	}
}