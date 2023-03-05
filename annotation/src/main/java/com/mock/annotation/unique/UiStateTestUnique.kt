package com.mock.annotation.unique


/**
 * todo exception：只能有一种array；
 * todo enum类型检测；sealed类型检测
 * todo int string 拆开
 */
@Retention(value = AnnotationRetention.SOURCE)
@Target(AnnotationTarget.FIELD)
annotation class UiStateTestUnique(
    val intDef: IntArray = [],
    val stringDef: Array<String> = [],
    val classDef: Array<String> = [],
)
