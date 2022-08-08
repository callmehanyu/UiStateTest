package com.mock.util

fun String.toUpperCaseInFirst(): String = this.mapIndexed { index, c ->
    if (index == 0) {
        c.toUpperCase()
    } else {
        c
    }
}.joinToString("")

fun String.toLowerCaseInFirst(): String = this.mapIndexed { index, c ->
    if (index == 0) {
        c.toLowerCase()
    } else {
        c
    }
}.joinToString("")

fun String.lastName(): String = this.split(".").last()