package com.mock.annotation.limit

/**
 * Boolean
 */
@Retention(value = AnnotationRetention.SOURCE)
@Target(AnnotationTarget.FIELD)
annotation class UiStateTestLimitIntDef(
    val cases: Array<IntDef> = [],
)

/**
 * IntDef
 */
@Retention(value = AnnotationRetention.SOURCE)
@Target(AnnotationTarget.ANNOTATION_CLASS)
annotation class IntDef(
    val value: Int,
    val limitIdList: Array<String>,
)
