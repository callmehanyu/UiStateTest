package com.zhy.unittest

import com.mock.annotation.UiStateTestDeclared
import com.mock.annotation.custom.UiStateTestCustomString
import com.mock.annotation.unique.UiStateTestUnique
import com.zhy.demo.InterState2

@UiStateTestDeclared
data class InterState(
//    @UiStateTestUnique(intDef = [9,99])
//    val interInt: Int = 99,
    @UiStateTestCustomString("内部str")
    val interString: String = "内部str",
//    @UiStateTestUnique
//    val inter2: InterState2 = InterState2(),
)


/**
 * 测试多个属性
 */

//@UiStateTest
//data class TestUiStateMultiple1(
//    @UiStateTestUnique
//    val isFast: Boolean,
//    @UiStateTestUnique
//    val inter: InterState = InterState(),
//)
//
//// todo
//@UiStateTest
//data class TestUiStateMultiple2(
//    @UiStateTestUnique
//    val inter: InterState = InterState(),
//    @UiStateTestUnique
//    val isFast: Boolean,
//)
//
//@UiStateTest
//data class TestUiStateMultiple3(
//    @UiStateTestUnique
//    val myEnum: MyEnum = MyEnum.NORMAL,
//    @UiStateTestUnique
//    val inter: InterState = InterState(),
//)
//
//@UiStateTest
//data class TestUiStateMultiple4(
//    @UiStateTestUnique
//    val inter: InterState = InterState(),
//    @UiStateTestUnique
//    val myEnum: MyEnum = MyEnum.NORMAL,
//)
//
//@UiStateTest
//data class TestUiStateMultiple5(
//    @UiStateTestUnique
//    val isFast: Boolean,
//    @UiStateTestUnique
//    val myEnum: MyEnum = MyEnum.NORMAL,
//)
//
//@UiStateTest
//data class TestUiStateMultiple6(
//    @UiStateTestUnique
//    val myEnum: MyEnum = MyEnum.NORMAL,
//    @UiStateTestUnique
//    val isFast: Boolean,
//)
//
//@UiStateTest
//data class TestUiStateMultiple7(
//    @UiStateTestUnique
//    val isFast: Boolean,
//    @UiStateTestUnique
//    val inter: InterState = InterState(),
//    @UiStateTestUnique
//    val isSlow: Boolean,
//)
