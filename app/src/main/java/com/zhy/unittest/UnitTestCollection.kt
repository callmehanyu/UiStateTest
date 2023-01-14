package com.zhy.unittest

import android.util.Log
import com.mock.annotation.UiStateTest
import com.mock.annotation.custom.UiStateTestCustomInt
import com.mock.annotation.custom.UiStateTestCustomString
import com.mock.annotation.unique.UiStateTestUnique
import com.zhy.collection.uistate.UiState

/**
 * todo 一刻设置页
 */
@UiStateTest
data class TestUiStateCollection(
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
) : UiState() {

    override fun equalsUnique(other: Any?): Boolean {
        Log.d("needCollect", "equalsUnique")

        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        if (other !is TestUiStateCollection) return false

        if (myEnum != other.myEnum) return false
        if (hisEnum != other.hisEnum) return false

        return true
    }

    override fun hasTodo(): Boolean {
//        if (tv1Cnt is Nothing) {
//            throw Exception()
//        }
        return true
    }

}