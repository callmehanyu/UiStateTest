package com.zhy.uistate.app

import com.zhy.unittest.TestUiStateLimit1
import kotlin.collections.List

/**
 * com.zhy.unittest.TestUiStateLimit1_0
 */
public val testUiStateLimit1_0: TestUiStateLimit1 = TestUiStateLimit1(
    isFaast = true,
    isSllow = true,
    )

/**
 * com.zhy.unittest.TestUiStateLimit1_1
 */
public val testUiStateLimit1_1: TestUiStateLimit1 = TestUiStateLimit1(
    isFaast = true,
    isSllow = false,
    )

/**
 * com.zhy.unittest.TestUiStateLimit1_List
 */
public val testUiStateLimit1_List: List<TestUiStateLimit1> = listOf(testUiStateLimit1_0,
    testUiStateLimit1_1, )
