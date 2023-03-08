package mock

import com.mock.annotation.*
import com.mock.annotation.custom.UiStateTestCustomDeclared
import com.mock.annotation.custom.UiStateTestCustomInt
import com.mock.annotation.custom.UiStateTestCustomString
import com.mock.annotation.limit.*
import com.mock.annotation.unique.UiStateTestEnum
import com.mock.annotation.unique.UiStateTestSealed
import com.mock.annotation.unique.UiStateTestUnique
import mock.casebuilder.CaseFactory
import mock.casebuilder.UiStateCase
import mock.property.DeclaredProperty
import mock.tree.Tree
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement
import javax.lang.model.util.Elements
import javax.lang.model.util.Types

internal lateinit var messager: Messager
private const val KAPT_ARGUMENTS_ARG_KEY_GENERATE_FILE_PATH = "uiStateTestGenerateFilePath"
private const val KAPT_ARGUMENTS_ARG_KEY_GENERATE_FILE_PACKAGE_NAME = "uiStateTestGenerateFilePackageName"

internal class UiStateTestProcessor : AbstractProcessor() {

    private lateinit var elementUtils: Elements
    private lateinit var filer: Filer
    private lateinit var typeUtils: Types
    private var generateFilePath: String? = null
    private var generateFilePackageName: String? = null

    private val elementEnumSet by lazy { mutableSetOf<Element>() }
    private val elementSealedSet by lazy { mutableSetOf<Element>() }
    private val elementsDeclaredSet by lazy { mutableSetOf<Element>() }
    private val declaredCaseTreeList: MutableList<Tree> by lazy { mutableListOf() }

    private val caseFactory by lazy {
        CaseFactory(
            elementEnumSet,
            elementSealedSet,
            declaredCaseTreeList,
            generateFilePath ?: "",
            generateFilePackageName ?: ""
        )
    }

    private val sourceFileGenerator by lazy {
        SourceFileGenerator(
            elementEnumSet,
            elementSealedSet,
            generateFilePath ?: "",
            generateFilePackageName ?: "",
            "Complier",
        )
    }

    override fun init(processingEnv: ProcessingEnvironment) {
        super.init(processingEnv)
        typeUtils = processingEnv.typeUtils
        elementUtils = processingEnv.elementUtils
        filer = processingEnv.filer
        messager = processingEnv.messager

        val options = processingEnv.options
        generateFilePath = options[KAPT_ARGUMENTS_ARG_KEY_GENERATE_FILE_PATH]
        generateFilePackageName = options[KAPT_ARGUMENTS_ARG_KEY_GENERATE_FILE_PACKAGE_NAME]

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
        annotations.add(UiStateTestCustomDeclared::class.java.canonicalName)

        return annotations
    }

    override fun process(
        annotations: MutableSet<out TypeElement>,
        roundEnv: RoundEnvironment
    ): Boolean {

        /**
         * 0.
         */
        collectElementEnumAndSealedSet(roundEnv)

        /**
         * 1.对每个 UiState，建树
         */
        val caseTreeList = UiStateCase(
            roundEnv,
            elementEnumSet,
            elementSealedSet,
            this.declaredCaseTreeList,
            generateFilePath ?: "",
            generateFilePackageName ?: "",
        ).build()
        if (caseTreeList.isEmpty()) {
            return true
        }

        /**
         * 2.生成代码
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

        /**
         * 0.对每个 UiStateTestDeclared中的自定义数据结构，建立树
         */
        val declaredCaseTreeList = buildUiStateTestDeclaredTreeList(this.elementsDeclaredSet)
        this.declaredCaseTreeList.addAll(declaredCaseTreeList)

    }

    private fun buildUiStateTestDeclaredTreeList(elementsAnnotatedWith: Set<Element>): List<Tree> {
        return elementsAnnotatedWith
            .asSequence()
            .filter { it.kind.isClass && it is TypeElement }
            .map {
                val root = Tree(
                    DeclaredProperty(
                        it,
                        it.simpleName.toString(),
                        isLast = false
                    )
                )
                caseFactory.buildCompleteCasesDeclared(root)
                root
            }
            .toList()
    }

}
