package com.gray.annotation_demo

//没变化
private const val DIFF_TYPE_NOTHING = 0

// 普通类型删除
private const val DIFF_TYPE_NORMAL_TYPE_DELETE = 1
// 普通类型新增
private const val DIFF_TYPE_NORMAL_TYPE_ADD = 2
// 枚举类型删除
private const val DIFF_TYPE_ENUM_TYPE_DELETE = 3
// 枚举类型新增
private const val DIFF_TYPE_ENUM_TYPE_ADD = 4
// 枚举范围删除
private const val DIFF_TYPE_ENUM_VALUE_DELETE = 5
// 枚举范围新增
private const val DIFF_TYPE_ENUM_VALUE_ADD = 6
// 自定义类型删除
private const val DIFF_TYPE_CUSTOM_TYPE_DELETE = 7
// 自定义类型新增
private const val DIFF_TYPE_CUSTOM_TYPE_ADD = 8

// 全部变化
private const val DIFF_TYPE_ALL = 9
//
//internal fun updateUiTestCases(typeElement: TypeElement, elementEnum: List<Element>) {
//    calculateDiff().forEach { diffType ->
//        when(diffType) {
//            DIFF_TYPE_NOTHING -> {
//                // do nothing
//            }
//            DIFF_TYPE_ALL -> {
//                diffTypeAll(typeElement,elementEnum)
//            }
//        }
//    }
//
//}

private fun calculateDiff(): List<Int> {
    return listOf(DIFF_TYPE_ALL)
}