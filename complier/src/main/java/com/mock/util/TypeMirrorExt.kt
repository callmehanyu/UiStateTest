package com.mock.util

import javax.lang.model.element.Element
import javax.lang.model.type.TypeKind
import javax.lang.model.type.TypeMirror

internal val typeKindInt = listOf(TypeKind.BYTE, TypeKind.SHORT, TypeKind.INT, TypeKind.LONG)

internal fun TypeMirror.isClass() = kind == TypeKind.DECLARED && !this.isString()

internal fun TypeMirror.isString() = this.toString() == "java.lang.String"

internal fun TypeMirror.isList() = this.toString().split("<").firstOrNull() == "java.util.List"
internal fun TypeMirror.getGenerics() = this.toString().split("<")[1].split(">").firstOrNull()

fun String.isPrimitiveType(): Boolean {
    return when (this) {
        "java.lang.Boolean", "java.lang.Byte", "java.lang.Short", "java.lang.Integer", "java.lang.Long", "java.lang.Character", "java.lang.Float", "java.lang.Double" -> true
        else -> false
    }
}

internal fun TypeMirror.findEnum(elementEnumSet: Set<Element>) =
    elementEnumSet.find { it.asType() == this }

internal fun TypeMirror.isEnum(elementEnumSet: Set<Element>) = findEnum(elementEnumSet) != null

internal fun TypeMirror.findSealed(elementSealedSet: Set<Element>) =
    elementSealedSet.find { it.asType() == this }

internal fun TypeMirror.isSealed(elementSealedSet: Set<Element>) =
    findEnum(elementSealedSet) != null