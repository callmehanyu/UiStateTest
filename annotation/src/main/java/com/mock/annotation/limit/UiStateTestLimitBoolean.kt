package com.mock.annotation.limit


/**
 * Boolean
 */
@Retention(value = AnnotationRetention.SOURCE)
@Target(AnnotationTarget.FIELD)
annotation class UiStateTestLimitBoolean(
    val cases: Array<Boolean> = [],
)

/**
 * Boolean
 */
@Retention(value = AnnotationRetention.SOURCE)
@Target(AnnotationTarget.ANNOTATION_CLASS)
annotation class Boolean(
    val value: kotlin.Boolean,
    val limitIdList: Array<String>,
)


