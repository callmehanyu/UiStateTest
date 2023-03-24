package com.zhy.uistate.app

import com.zhy.demo.ATestOut
import kotlin.collections.List

/**
 * com.zhy.demo.ATestOut_0
 */
public val aTestOut_0: ATestOut = ATestOut(
    all = com.zhy.demo.ATestAll(
    aaaA = true,
    ),
    cccC = true,
    )

/**
 * com.zhy.demo.ATestOut_1
 */
public val aTestOut_1: ATestOut = ATestOut(
    all = com.zhy.demo.ATestAll(
    aaaA = true,
    ),
    cccC = false,
    )

/**
 * com.zhy.demo.ATestOut_2
 */
public val aTestOut_2: ATestOut = ATestOut(
    all = com.zhy.demo.ATestAll(
    aaaA = false,
    ),
    cccC = true,
    )

/**
 * com.zhy.demo.ATestOut_3
 */
public val aTestOut_3: ATestOut = ATestOut(
    all = com.zhy.demo.ATestAll(
    aaaA = false,
    ),
    cccC = false,
    )

/**
 * com.zhy.demo.ATestOut_List
 */
public val aTestOut_List: List<ATestOut> = listOf(aTestOut_0, aTestOut_1, aTestOut_2, aTestOut_3, )
