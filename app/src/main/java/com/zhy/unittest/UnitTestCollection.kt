package com.zhy.unittest

import android.content.Context
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
    val rvList: List<VHParam> = emptyList(),
    @UiStateTestCustomDeclared("com.base.bitmap.BitmapUtils.getBitmap(android.app.Application().baseContext, java.io.File(android.os.Environment.getExternalStorageDirectory(), \"/timg.jpeg\"), true, 50,50)")
    val bmp: Bitmap? = null,
    @UiStateTestUnique(
        classDef = [
            "com.base.bitmap.BitmapUtils.getBitmap(android.app.Application().baseContext, java.io.File(android.os.Environment.getExternalStorageDirectory(), \"/timg.jpeg\"), true, 50,50)",
            "com.base.bitmap.BitmapUtils.getBitmap(android.app.Application().baseContext, java.io.File(android.os.Environment.getExternalStorageDirectory(), \"/timg2.jpeg\"), true, 50,50)",
        ]
    )
    val bmpList: List<Bitmap?> = emptyList(),
) {

//    override fun equalsUnique(other: Any?): Boolean {
//        Log.d("needCollect", "equalsUnique")
//
//        if (this === other) return true
//        if (javaClass != other?.javaClass) return false
//        if (other !is TestUiStateCollection) return false
//
//        if (myEnum != other.myEnum) return false
//        if (hisEnum != other.hisEnum) return false
//
//        return true
//    }

//    override fun hasTodo(): Boolean {
////        if (tv1Cnt is Nothing) {
////            throw Exception()
////        }
//        return true
//    }

}

@UiStateTestDeclared
data class VHParam(
    @UiStateTestUnique(intDef = [23, 34])
    val a: Int,
    @UiStateTestCustomString("VHParambbb")
    val b: String
)