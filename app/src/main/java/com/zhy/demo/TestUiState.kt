package com.zhy.demo

import androidx.annotation.IntDef
import com.mock.annotation.unique.UiStateTestUnique

internal const val MAIN_ACTIVITY_STATE_CHANGE_TV1CNT = 0
internal const val MAIN_ACTIVITY_EVENT_CHANGE_BTN1STRING = 1

abstract class UiState {
    // todo 三种状态：1.可用，2。不存在的状态；3.可用，但是存在todo的状态
    open fun isIllegalCheck(): Boolean = true
}

const val MAN = 0x2
const val WOMEN = 0x3

const val BLACK = "black"
const val WHITE = "white"



@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.FIELD, AnnotationTarget.FUNCTION)
@MustBeDocumented
@IntDef(MAN, WOMEN)
@Retention(AnnotationRetention.SOURCE)
annotation class Sex

/**
 * boolean enum sealed intdef stringdef
 */
//@UiStateTest
data class TestUiState(

//    @UiStateTestUnique
//    val isFast: Boolean,
//
//    @UiStateTestUnique
//    val isSlow: Boolean,
//
//    @UiStateTestUnique
//    val myEnum: MyEnum = MyEnum.NORMAL,

//    @UiStateTestUnique
//    val guide: Guide = DialogGuide("","",""),

//    @UiStateTestUnique(intDef = [MAN, WOMEN])
//    val sex: Int = WOMEN,
//
//    @UiStateTestUnique(stringDef = [BLACK, WHITE])
//    val color: String = BLACK,
//
    val tv1Cnt: Int = 0,


    val btn1String: String? = "init",

////    @UiStateTestUnique
//    val inter: InterState = InterState(),
//    @UiStateTestUnique
    val inter2: InterState2 = InterState2(),
) : UiState() {

    override fun isIllegalCheck(): Boolean {
        return super.isIllegalCheck()

    }

//    override fun toString(): String {
//        return "TestUiState(tv1Cnt==${tv1Cnt}, btn1String==${btn1String}, " +
//                "interState=(InterState(interString==${interState.interString}, interInt==${interState.interInt}))"
//    }

}



////@UiStateTestDeclared
//data class NestedState(
//
//    val nestedString: String = "嵌套str",
//    @UiStateTestUnique(intDef = [888,8888])
//    val nestedInt: Int = 888,
//)

//@UiStateTestDeclared
data class InterState2(

    val interString2: String = "内部str2",
    @UiStateTestUnique(intDef = [8,88])
    val interInt2: Int = 88,
)





