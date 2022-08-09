package com.zhy.unittest

import com.mock.annotation.UiStateTestDeclared
import com.mock.annotation.unique.UiStateTestUnique

data class Baby(val face: Int)

@UiStateTestDeclared
data class Boy(val head: Int)

@UiStateTestDeclared
data class Girl(

    @UiStateTestUnique([4,5])
    val head: Int
    )

//@UiStateTest
//data class TestUiStateList1(
//    @UiStateTestUnique
//    val members: List<Int>,
//)
//
//@UiStateTest
//data class TestUiStateList2(
//    @UiStateTestUnique
//    val members: List<Baby>,
//)

//@UiStateTest
//data class TestUiStateList3(
//    @UiStateTestUnique
//    val boys: List<Boy>,
//)

//@UiStateTest
//data class TestUiStateList4(
//    @UiStateTestUnique
//    val girls: List<Girl>,
//)


//@UiStateTest
//data class TestUiStateSingle9(
//    @UiStateTestUnique
//    val birthdayMap: Map<Int, Int>,
//)