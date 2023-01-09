package com.mock.annotation.limit

import kotlin.reflect.KClass

/**
 * Boolean
 * todo 没讲清楚
 */
@Retention(value = AnnotationRetention.SOURCE)
@Target(AnnotationTarget.FIELD)
annotation class UiStateTestLimitEnum(
    val cases: Array<Enum> = [],
)

/**
 * enum
 */
@Retention(value = AnnotationRetention.SOURCE)
@Target(AnnotationTarget.ANNOTATION_CLASS)
annotation class Enum(
    val value: String,
    val limitIdList: Array<String>,
)
