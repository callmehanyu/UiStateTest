package com.zhy.demo

import com.mock.annotation.UiStateTest
import com.mock.annotation.UiStateTestDeclared
import com.mock.annotation.unique.UiStateTestUnique

@UiStateTest
data class ATestOut(
	@UiStateTestUnique
	val all: ATestAll,
	@UiStateTestUnique
	val cccC: Boolean?
)
@UiStateTestDeclared
data class ATestAll(
	@UiStateTestUnique
	val aaaA: Boolean,
	val bbbB: Boolean = false,
)
