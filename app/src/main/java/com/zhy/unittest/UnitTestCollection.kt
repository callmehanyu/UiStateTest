package com.zhy.unittest

import com.mock.annotation.UiStateTest
import com.mock.annotation.limit.UiStateTestLimitEnum
import com.mock.annotation.unique.UiStateTestUnique

@UiStateTest
data class TestUiStateCollection1(
    @UiStateTestLimitEnum([com.mock.annotation.limit.Enum(value = "CHRISTMAS", limitIdList = ["chinses"])])
    @UiStateTestUnique
    val myEnum: MyEnum = MyEnum.NORMAL,
    @UiStateTestLimitEnum([com.mock.annotation.limit.Enum(value = "NORMAL", limitIdList = ["chinses"])])
    @UiStateTestUnique
    val hisEnum: MyEnum = MyEnum.NORMAL,
    val tv1Cnt: Int = 0,
    val btn1String: String? = "init",
)