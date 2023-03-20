package com.zhy.unittest

import android.graphics.Bitmap
import com.mock.annotation.UiStateTest
import com.mock.annotation.UiStateTestDeclared
import com.mock.annotation.custom.UiStateTestCustomDeclared
import com.mock.annotation.custom.UiStateTestCustomInt
import com.mock.annotation.custom.UiStateTestCustomString
import com.mock.annotation.unique.UiStateTestUnique

/**
 *
 */
@UiStateTest
data class TestUiState(
//    @UiStateTestLimitEnum([com.mock.annotation.limit.Enum(value = "CHRISTMAS", limitIdList = ["chinses"])])
    @UiStateTestUnique
    val myEnum: MyEnum = MyEnum.NORMAL,
//    @UiStateTestLimitEnum([com.mock.annotation.limit.Enum(value = "NORMAL", limitIdList = ["chinses"])])
    @UiStateTestUnique
    val hisEnum: MyEnum = MyEnum.NORMAL,
    @UiStateTestCustomInt(999)
    val tv1Cnt: Int = 0,
    @UiStateTestCustomString("asdf")
    val btn1String: String? = "init",
    @UiStateTestUnique
    val vh: VHParam = VHParam(444, "hhh", 'b'),
    @UiStateTestUnique
    val rvList: List<VHParam> = emptyList(),
    @UiStateTestCustomDeclared("com.zhy.uistate.bitmap")
    val bmp: Bitmap? = null,
    @UiStateTestUnique(
        classDef = [
            "com.zhy.uistate.bitmap",
            "com.zhy.uistate.bitmap",
        ]
    )
    val bmpList: List<Bitmap?> = emptyList(),
)

@UiStateTestDeclared
data class VHParam(
    @UiStateTestUnique(intDef = [23, 34])
    val a: Int,
    @UiStateTestCustomString("VHParambbb")
    val b: String,
    @UiStateTestCustomDeclared("'A'")
    val initials: Char,
)