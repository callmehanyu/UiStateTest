package com.mock.annotation

/**
 * todo 如果 class 里面的属性没有unique注解，生成的uistate会有bug
 */
@Retention(value = AnnotationRetention.SOURCE)
@Target(AnnotationTarget.CLASS)
annotation class UiStateTestDeclared
