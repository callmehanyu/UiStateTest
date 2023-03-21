package com.mock.annotation

/**
 * 生成所有 uiState case
 * @param isOpen 是否开启。关闭以防止已经修改的代码被覆盖
 */
@Retention(value = AnnotationRetention.SOURCE)
@Target(AnnotationTarget.CLASS)
annotation class UiStateTest(
	val isOpen: Boolean = true,
)
