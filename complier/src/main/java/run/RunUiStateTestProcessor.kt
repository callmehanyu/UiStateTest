package run

import com.mock.annotation.RunUiStateTest
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.SourceVersion
import javax.lang.model.element.ElementKind
import javax.lang.model.element.TypeElement

internal class RunUiStateTestProcessor : AbstractProcessor() {

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
				SourceFileGenerator().generateSourceFile(it)
			}

		return true
	}

}