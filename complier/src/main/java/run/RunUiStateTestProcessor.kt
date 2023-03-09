package run

import com.mock.annotation.RunUiStateTest
import mock.KAPT_ARGUMENTS_ARG_KEY_GENERATE_FILE_PACKAGE_NAME
import mock.KAPT_ARGUMENTS_ARG_KEY_GENERATE_FILE_PATH
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.SourceVersion
import javax.lang.model.element.ElementKind
import javax.lang.model.element.TypeElement

internal class RunUiStateTestProcessor : AbstractProcessor() {

	private var generateFilePath: String? = null
	private var generateFilePackageName: String? = null

	override fun init(processingEnv: ProcessingEnvironment) {
		super.init(processingEnv)
		val options = processingEnv.options
		generateFilePath = options[KAPT_ARGUMENTS_ARG_KEY_GENERATE_FILE_PATH]
		generateFilePackageName = options[KAPT_ARGUMENTS_ARG_KEY_GENERATE_FILE_PACKAGE_NAME]
	}

	override fun getSupportedSourceVersion(): SourceVersion {
		return SourceVersion.latestSupported()
	}

	override fun getSupportedAnnotationTypes(): MutableSet<String> {
		val annotations: MutableSet<String> = LinkedHashSet()
		annotations.add(RunUiStateTest::class.java.canonicalName)
		return annotations
	}

	override fun process(
		annotations: MutableSet<out TypeElement>,
		roundEnv: RoundEnvironment
	): Boolean {
		roundEnv.getElementsAnnotatedWith(RunUiStateTest::class.java)
			.filter { it.kind == ElementKind.METHOD }
			.forEach {
				SourceFileGenerator(
					generateFilePath ?: "",
					generateFilePackageName ?: "",
				).generateSourceFile(it)
			}

		return true
	}

}