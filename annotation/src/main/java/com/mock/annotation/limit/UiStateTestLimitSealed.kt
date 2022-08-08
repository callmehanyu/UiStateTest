package com.mock.annotation.limit

import kotlin.reflect.KClass

/**
 * Boolean
 */
@Retention(value = AnnotationRetention.SOURCE)
@Target(AnnotationTarget.FIELD)
annotation class UiStateTestLimitSealed(
    val cases: Array<Sealed> = [],
)

/**
 * sealed
 */
@Retention(value = AnnotationRetention.SOURCE)
@Target(AnnotationTarget.ANNOTATION_CLASS)
annotation class Sealed(
    val value: KClass<*>,
    val limitIdList: Array<String>,
)
