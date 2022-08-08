package com.gray.complier

import com.gray.annotation.Binding
import com.squareup.javapoet.ClassName
import com.squareup.javapoet.JavaFile
import com.squareup.javapoet.MethodSpec
import com.squareup.javapoet.TypeSpec
import java.util.*
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.*
import javax.lang.model.util.Elements
import javax.lang.model.util.Types
import javax.tools.Diagnostic
import kotlin.collections.LinkedHashSet


class BindProcessor : AbstractProcessor() {
    private lateinit var messager: Messager
    private lateinit var elementUtils: Elements
    private lateinit var filer: Filer
    private lateinit var typeUtils: Types
    override fun process(
        annotations: MutableSet<out TypeElement>,
        roundEnv: RoundEnvironment
    ): Boolean {

        roundEnv.getElementsAnnotatedWith(Binding::class.java).filter { it.kind.isField }.forEach {
            val variableElement = it as VariableElement
            val packageOf = elementUtils.getPackageOf(variableElement)
            val annotation = variableElement.getAnnotation(Binding::class.java)
            var element: Element = variableElement
            while (element.kind != ElementKind.CLASS) {
                element = element.enclosingElement
            }
            kotlin.runCatching {
                CodeProcess(
                    variableElement,
                    packageOf,
                    element as TypeElement,
                    annotation.text
                ).createCode(filer)
            }.onFailure { e ->
                messager.printMessage(Diagnostic.Kind.WARNING, e.localizedMessage)
            }
        }

        return true

    }

    override fun init(processingEnv: ProcessingEnvironment) {
        super.init(processingEnv)
        typeUtils = processingEnv.typeUtils
        elementUtils = processingEnv.elementUtils
        filer = processingEnv.filer
        messager = processingEnv.messager

    }

    override fun getSupportedSourceVersion(): SourceVersion {
        return SourceVersion.latestSupported()
    }

    override fun getSupportedAnnotationTypes(): MutableSet<String> {
        val annotations: MutableSet<String> = LinkedHashSet()
        annotations.add(Binding::class.java.canonicalName)
        return annotations
    }

}