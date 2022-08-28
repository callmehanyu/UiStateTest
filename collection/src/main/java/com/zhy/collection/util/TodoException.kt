package com.zhy.collection.util

internal const val TODO = "com.zhy.collection.util.TODOException()"

public fun TODOException(): Nothing {
    throw TodoException()
}

class TodoException : Exception()