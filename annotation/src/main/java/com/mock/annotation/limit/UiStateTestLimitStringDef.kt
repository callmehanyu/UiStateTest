package com.mock.annotation.limit

/**
 * Boolean
 */
@Retention(value = AnnotationRetention.SOURCE)
@Target(AnnotationTarget.FIELD)
annotation class UiStateTestLimitStringDef(
    val cases: Array<StringDef> = [],
)

/**
 * StringDef
 */
@Retention(value = AnnotationRetention.SOURCE)
@Target(AnnotationTarget.ANNOTATION_CLASS)
annotation class StringDef(
    val value: String,
    val limitIdList: Array<String>,
)