package com.mock.annotation

@Retention(value = AnnotationRetention.SOURCE)
@Target(AnnotationTarget.FILE)
annotation class UiStateTestCollection(
    val className: String,
    val property: String,
)