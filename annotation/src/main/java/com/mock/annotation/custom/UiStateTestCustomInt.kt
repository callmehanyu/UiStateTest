package com.mock.annotation.custom

@Retention(value = AnnotationRetention.SOURCE)
@Target(AnnotationTarget.FIELD)
annotation class UiStateTestCustomInt(
    val customInt: Int
)
