package com.gray.annotation

@Retention(value = AnnotationRetention.BINARY)
@Target(AnnotationTarget.FIELD)
annotation class Binding(val text: String)