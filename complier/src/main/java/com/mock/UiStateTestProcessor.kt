package com.mock

import com.mock.annotation.*
import com.mock.annotation.custom.UiStateTestCustomInt
import com.mock.annotation.custom.UiStateTestCustomString
import com.mock.annotation.limit.*
import com.mock.annotation.unique.UiStateTestEnum
import com.mock.annotation.unique.UiStateTestSealed
import com.mock.annotation.unique.UiStateTestUnique
import com.mock.vo.*
import com.mock.vo.DeclaredProperty
import com.mock.vo.PrimitiveProperty
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement
import javax.lang.model.util.Elements
import javax.lang.model.util.Types
import javax.tools.Diagnostic


internal class UiStateTestProcessor : AbstractProcessor() {

    private lateinit var messager: Messager
    private lateinit var elementUtils: Elements
    private lateinit var filer: Filer
    private lateinit var typeUtils: Types

    private val elementEnumSet by lazy { mutableSetOf<Element>() }
    private val elementSealedSet by lazy { mutableSetOf<Element>() }
    private val elementsDeclaredSet by lazy { mutableSetOf<Element>() }

    private val caseBuilder by lazy {
        CaseBuilder(
            elementEnumSet,
            elementSealedSet,
            elementsDeclaredSet,
            messager
        )
    }
    private val sourceFileGenerator by lazy {
        SourceFileGenerator(
            elementEnumSet,
            elementSealedSet,
            UiStateTestCollection("",""),
            messager
        )
    }
    private val treePruner by lazy { TreePruner(elementEnumSet, elementSealedSet, messager) }

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
        annotations.add(UiStateTest::class.java.canonicalName)

        annotations.add(UiStateTestUnique::class.java.canonicalName)

        annotations.add(UiStateTestEnum::class.java.canonicalName)
        annotations.add(UiStateTestSealed::class.java.canonicalName)
        annotations.add(UiStateTestDeclared::class.java.canonicalName)

        annotations.add(UiStateTestLimitBoolean::class.java.canonicalName)
        annotations.add(com.mock.annotation.limit.Boolean::class.java.canonicalName)
        annotations.add(UiStateTestLimitIntDef::class.java.canonicalName)
        annotations.add(com.mock.annotation.limit.IntDef::class.java.canonicalName)
        annotations.add(UiStateTestLimitStringDef::class.java.canonicalName)
        annotations.add(com.mock.annotation.limit.StringDef::class.java.canonicalName)
        annotations.add(UiStateTestLimitEnum::class.java.canonicalName)
        annotations.add(com.mock.annotation.limit.Enum::class.java.canonicalName)
        annotations.add(UiStateTestLimitSealed::class.java.canonicalName)
        annotations.add(com.mock.annotation.limit.Sealed::class.java.canonicalName)

//        annotations.add(UiStateTestCollection::class.java.canonicalName)
        annotations.add(UiStateTestCustomString::class.java.canonicalName)
        annotations.add(UiStateTestCustomInt::class.java.canonicalName)


        return annotations
    }

    override fun process(
        annotations: MutableSet<out TypeElement>,
        roundEnv: RoundEnvironment
    ): Boolean {

        collectElementEnumAndSealedSet(roundEnv)

        /**
         * 1.对每个 UiState，建树
         */
        val caseTreeList = buildUiStateTestTreeList(roundEnv)
        if (caseTreeList.isEmpty()) {
            return true
        }

        /**
         * 2.对每个 UiStateTestDeclared中的自定义数据结构，建立树
         */
        val declaredCaseTreeList = buildUiStateTestDeclaredTreeList(elementsDeclaredSet)

        /**
         * 3.对于有嵌套关系的树，嫁接在一起
         */
        caseTreeList.forEach { case ->
            graftTree(case, declaredCaseTreeList)
            treePruner.start(case)
        }

        /**
         * 4.生成代码
         */
        sourceFileGenerator.generateSourceFiles(caseTreeList)

        return true

    }

    /**
     *
     */
    private fun collectElementEnumAndSealedSet(roundEnv: RoundEnvironment) {
        val elementEnumSet = roundEnv.getElementsAnnotatedWith(UiStateTestEnum::class.java)
            .filter { it.kind.isClass }
        this.elementEnumSet.addAll(elementEnumSet)

        val elementSealedSet = roundEnv.getElementsAnnotatedWith(UiStateTestSealed::class.java)
            .filter { it.kind.isClass }
        this.elementSealedSet.addAll(elementSealedSet)

        val elementsDeclaredSet = roundEnv.getElementsAnnotatedWith(UiStateTestDeclared::class.java)
            .filter { it.kind.isClass }
        this.elementsDeclaredSet.addAll(elementsDeclaredSet)

    }

    /**
     *
     */
    private fun buildUiStateTestTreeList(roundEnv: RoundEnvironment): List<Tree> =
        roundEnv.getElementsAnnotatedWith(UiStateTest::class.java)
            .asSequence()
            .filter { it.kind.isClass && it is TypeElement }
            .map {
                val root = Tree(PrimitiveProperty(it, it.simpleName.toString(), isLast = true))
                caseBuilder.buildCompleteCases(root)
                root
            }
            .toList()

    private fun buildUiStateTestDeclaredTreeList(elementsAnnotatedWith: Set<Element>): List<Tree> {
        return elementsAnnotatedWith
            .asSequence()
            .filter { it.kind.isClass && it is TypeElement }
            .map {
                val root = Tree(
                    DeclaredProperty(
                        it,
                        it.simpleName.toString(),
    //                        isGrafted = false,
                        isLast = false
                    )
                )
                caseBuilder.buildCompleteCasesDeclared(root)
                root
            }
            .toList()
    }

}
