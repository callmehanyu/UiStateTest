package com.mock.annotation.custom

/**
 * 非可枚举自定义属性
 * 测试用例构造注解：自定义，非基本类型
 */
@Retention(value = AnnotationRetention.SOURCE)
@Target(AnnotationTarget.FIELD)
annotation class UiStateTestCustomDeclared(
	val instanceToString: String,
)
