package com.gray.annotation_demo

import com.mock.annotation.*
import com.mock.annotation.unique.UiStateTestEnum
import com.mock.annotation.unique.UiStateTestSealed
import com.mock.annotation.unique.UiStateTestUnique


@UiStateTestEnum
enum class MyEnum {
    NORMAL,
    SPRING, NEW_YEAR, CHRISTMAS
}

//@UiStateTestSealed
//sealed class GuideIn(val title: String, val content: String) {
//
//    class PopupGuideIn(
//        title: String, content: String,
//        val toast: String,
//    ) : GuideIn(title, content)
//
//    class DialogGuideIn(
//        title: String, content: String,
//        val confirmText: String,
//    ) : GuideIn(title, content)
//
//}

/**
 * TODO 暂时不支持
 */
//@UiStateTestSealed
//sealed class GuideOut(val title: String, val content: String)
//
//
//class PopupGuideOut(
//    title: String, content: String,
//    val toast: String,
//) : GuideOut(title, content)
//
//class DialogGuideOut(
//    title: String, content: String,
//    val confirmText: String,
//) : GuideOut(title, content)

//
//@UiStateTestDeclared
//internal sealed class GuideOutFile(val title: String, val content: String)




/**
 * 测试单个属性
 */

//@UiStateTest
//data class TestUiStateSingle1(
//    @UiStateTestUnique
//    val isFast: Boolean,
//)
//
//@UiStateTest
//data class TestUiStateSingle11(
//    val isFast: Boolean,
//)

//@UiStateTest
//data class TestUiStateSingle2(
//    @UiStateTestUnique
//    val myEnum: MyEnum = MyEnum.NORMAL,
//)

//@UiStateTest
//data class TestUiStateSingle3(
//    @UiStateTestUnique(intDef = [MAN, WOMEN])
//    val sex: Int = WOMEN,
//)
//
//@UiStateTest
//data class TestUiStateSingle4(
//    @UiStateTestUnique(stringDef = [BLACK, WHITE])
//    val color: String = BLACK,
//)
//
//@UiStateTest
//data class TestUiStateSingle41(
//    val color: String = BLACK,
//)
//
//@UiStateTest
//data class TestUiStateSingle5(
//    @UiStateTestUnique
//    val inter: InterState = InterState(),
//)
//
//@UiStateTest
//data class TestUiStateSingle51(
//    val inter: InterState = InterState(),
//)
//
//@UiStateTest
//data class TestUiStateSingle6(
//    @UiStateTestUnique
//    val guidein: GuideIn = GuideIn.PopupGuideIn("标题", "内容", "弹toast窗"),
//)
//
//@UiStateTest
//data class TestUiStateSingle61(
//    val guidein: GuideIn = GuideIn.PopupGuideIn("标题", "内容", "弹toast窗"),
//)
//
//@UiStateTest
//data class TestUiStateSingle7(
//    @UiStateTestUnique
//    val guideout: GuideOut = PopupGuideOut("标题", "内容", "弹toast窗"),
//)
//
//// todo
//@UiStateTest
//data class TestUiStateSingle71(
//    val guideout: GuideOut = PopupGuideOut("标题", "内容", "弹toast窗"),
//)




