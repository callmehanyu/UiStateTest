package com.zhy.uistate

import com.zhy.demo.Test2UiState
import kotlin.collections.List

/**
 * com.zhy.demo.Test2UiState_0
 */
public val test2UiState_0: Test2UiState = Test2UiState(
    inter = com.zhy.unittest.InterState(
    interString = "内部str",
    ),
    isFast = true,
    )

/**
 * com.zhy.demo.Test2UiState_1
 */
public val test2UiState_1: Test2UiState = Test2UiState(
    inter = com.zhy.unittest.InterState(
    interString = "内部str",
    ),
    isFast = false,
    )

/**
 * com.zhy.demo.Test2UiState_List
 */
public val test2UiState_List: List<Test2UiState> = listOf(test2UiState_0, test2UiState_1, )
