package com.zhy.collection.uistate

abstract class UiState {

    /**
     * 检查主键是否相同
     */
    abstract fun equalsUnique(other: Any?): Boolean

    /**
     * 检查是否存在Todo
     */
    abstract fun hasTodo(): Boolean
}