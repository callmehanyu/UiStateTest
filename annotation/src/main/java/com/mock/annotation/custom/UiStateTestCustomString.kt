package com.mock.annotation.custom

/**
 * 非可枚举String属性
 * todo 无法在 UiStateTestDeclared 的类中正确生成uistate
 */
@Retention(value = AnnotationRetention.SOURCE)
@Target(AnnotationTarget.FIELD)
annotation class UiStateTestCustomString(
    val customString: String
)
