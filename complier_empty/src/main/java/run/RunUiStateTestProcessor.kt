package run

import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.SourceVersion
import javax.lang.model.element.TypeElement

internal class RunUiStateTestProcessor : AbstractProcessor() {

	override fun init(processingEnv: ProcessingEnvironment) {
		super.init(processingEnv)
	}

	override fun getSupportedSourceVersion(): SourceVersion {
		return SourceVersion.latestSupported()
	}

	override fun getSupportedAnnotationTypes(): MutableSet<String> {
		return LinkedHashSet()
	}

	override fun process(
		annotations: MutableSet<out TypeElement>,
		roundEnv: RoundEnvironment
	): Boolean {
		return true
	}

}