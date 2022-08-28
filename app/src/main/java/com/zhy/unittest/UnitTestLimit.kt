package com.zhy.unittest

import com.mock.annotation.UiStateTest
import com.mock.annotation.limit.*
import com.mock.annotation.unique.UiStateTestUnique

@UiStateTest
data class TestUiStateLimit1(
    @UiStateTestLimitBoolean([com.mock.annotation.limit.Boolean(value = false, limitIdList = ["canSlow"])])
    @UiStateTestUnique
    val isFaast: Boolean,
    @UiStateTestUnique
    val isSllow: Boolean,
)
//
//@UiStateTest
//data class TestUiStateLimit2(
//    @UiStateTestLimitBoolean([com.mock.annotation.limit.Boolean(value = false, limitIdList = ["canSlow"])])
//    @UiStateTestUnique
//    val isFaast: Boolean,
//    @UiStateTestLimitBoolean([com.mock.annotation.limit.Boolean(value = false, limitIdList = ["canSlow"])])
//    @UiStateTestUnique
//    val isSllow: Boolean,
//)

//@UiStateTest
//data class TestUiStateLimit3(
//    @UiStateTestLimitIntDef([com.mock.annotation.limit.IntDef(value = 2, limitIdList = ["beMan"])])
//    @UiStateTestUnique(intDef = [2,3,4])
//    val sex: Int,
//    @UiStateTestLimitIntDef([com.mock.annotation.limit.IntDef(value = 30, limitIdList = ["beMan"])])
//    @UiStateTestUnique(intDef = [20,30,40])
//    val bitch: Int,
//)

//@UiStateTest
//data class TestUiStateLimit4(
//    @UiStateTestLimitStringDef([com.mock.annotation.limit.StringDef(value = "man", limitIdList = ["变态"])])
//    @UiStateTestUnique(stringDef = ["man","woman","中"])
//    val sexString: String,
//    @UiStateTestLimitStringDef([com.mock.annotation.limit.StringDef(value = "middle", limitIdList = ["变态"])])
//    @UiStateTestUnique(stringDef = ["low","middle","high"])
//    val bitch: String,
//)

@UiStateTest
data class TestUiStateLimit5(
    @UiStateTestLimitEnum([com.mock.annotation.limit.Enum(value = "CHRISTMAS", limitIdList = ["chinses"])])
    @UiStateTestUnique
    val myEnum: MyEnum = MyEnum.NORMAL,
    @UiStateTestLimitEnum([com.mock.annotation.limit.Enum(value = "NORMAL", limitIdList = ["chinses"])])
    @UiStateTestUnique
    val hisEnum: MyEnum = MyEnum.NORMAL,
)

//@UiStateTestSealed
//sealed class GuideLimitIn(val title: String, val content: String) {
//
//    class PopupLimitGuideIn(
//        title: String, content: String,
//        val toast: String,
//    ) : GuideLimitIn(title, content)
//
//    class DialogLimitGuideIn(
//        title: String, content: String,
//        val confirmText: String,
//    ) : GuideLimitIn(title, content)
//
//}
//
//@UiStateTest
//data class TestUiStateLimit6(
//    @UiStateTestLimitSealed([com.mock.annotation.limit.Sealed(value = (GuideLimitIn.PopupLimitGuideIn::class), limitIdList = ["guidelimitin1"])])
//    @UiStateTestUnique
//    val g1: GuideLimitIn,
//    @UiStateTestLimitSealed([com.mock.annotation.limit.Sealed(value = (GuideLimitIn.DialogLimitGuideIn::class), limitIdList = ["guidelimitin1"])])
//    @UiStateTestUnique
//    val g2: GuideLimitIn,
//)