package com.gray.complier

import com.squareup.javapoet.ClassName
import com.squareup.javapoet.JavaFile
import com.squareup.javapoet.MethodSpec
import com.squareup.javapoet.TypeSpec
import javax.annotation.processing.Filer
import javax.lang.model.element.Modifier
import javax.lang.model.element.PackageElement
import javax.lang.model.element.TypeElement
import javax.lang.model.element.VariableElement


class CodeProcess(
    val element: VariableElement,
    pack: PackageElement,
    type: TypeElement,
    var text: String
) {
    var name: String = element.simpleName.toString()
    var packNameString: String = pack.toString()
    var codeClazzName = type.simpleName.toString() + "Binding"
    fun createCode(filer: Filer) {

        val methodBuild = MethodSpec.methodBuilder("create")
            .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
            .returns(ClassName.get(element.asType()))
            .addStatement(" return new \$T(\$S)", ClassName.get(element.asType()), text)
            .build()
        val typeBuild = TypeSpec.classBuilder(codeClazzName)
            .addModifiers(Modifier.PUBLIC)
            .addMethod(methodBuild)
            .build()

        JavaFile.builder(packNameString, typeBuild)
            .build()
            .writeTo(filer)

    }

    override fun toString(): String {
        return "CodeProcess(element=$element, text='$text', name='$name', packNameString='$packNameString', codeClazzName='$codeClazzName')"
    }
}