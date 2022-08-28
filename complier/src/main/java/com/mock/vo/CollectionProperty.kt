package com.mock.vo

internal data class CollectionProperty(
    val name: String,
    val value: String,
) {

    override fun toString(): String {
        return "$name = $value"
    }

}

internal fun List<CollectionProperty>.equalsTo(other: List<Property>): Boolean {
    if (this.size != other.size) {
        return false
    }
    for (i in this.indices) {
        if (this[i].toString() != other[i].toString()) {
            return false
        }
    }
    return true
}