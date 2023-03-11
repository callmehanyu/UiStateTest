package com.zhy.demo

import com.mock.annotation.UiStateTest
import com.mock.annotation.UiStateTestDeclared
import com.mock.annotation.custom.UiStateTestCustomString
import com.mock.annotation.unique.UiStateTestUnique
import com.zhy.unittest.InterState


const val MAN = 0x2
const val WOMEN = 0x3

const val BLACK = "black"
const val WHITE = "white"

/**
 * boolean enum sealed intdef stringdef
 */
@UiStateTest
data class Test2UiState(
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

//	@UiStateTestCustomString("init")
//	val btn1String: String? = "init",

    @UiStateTestUnique
	val inter: InterState = InterState(),

	@UiStateTestUnique
	val isFast: Boolean,
)


////@UiStateTestDeclared
//data class NestedState(
//
//    val nestedString: String = "嵌套str",
//    @UiStateTestUnique(intDef = [888,8888])
//    val nestedInt: Int = 888,
//)

@UiStateTestDeclared
data class InterState2(
	@UiStateTestCustomString("fsid")
	val interString2: String = "内部str2",
	@UiStateTestUnique(intDef = [8, 88])
	val interInt2: Int = 88,
)





