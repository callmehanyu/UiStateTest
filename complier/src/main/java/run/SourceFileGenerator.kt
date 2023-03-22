@file:Suppress("JAVA_MODULE_DOES_NOT_EXPORT_PACKAGE")

package run

import code.*
import code.AS
import code.CLASS_JAVA
import code.ROOT_VIEW
import code.VAL
import com.squareup.kotlinpoet.*
import com.sun.tools.javac.code.Symbol
import mock.util.lastName
import mock.util.toLowerCaseInFirst
import java.io.File
import javax.lang.model.element.Element

private const val PACKAGE_LAUNCHER = "launcher"
private const val PACKAGE_SCREENSHOT = "screenshot"
private const val PACKAGE_BITMAP = "bitmap"
private const val PACKAGE_FILE = "file"

private const val CLASS_LAUNCHER = "UiStateTestLauncherActivity"

private const val FUN_NAME_SCREENSHOT = "screenShot"
private const val LAUNCHER = "launcher"
private const val INDEX = "index"
private const val UI_STATE = "uiState"

/**
 * 代码生成器
 */
internal class SourceFileGenerator(
	private val generateFilePath: String,
	private val generateFilePackageName: String,
) {

	private val dir = File(generateFilePath).apply {
		mkdirs()
	}

	fun generateSourceFile(element: Element) {

		/**
		 * 需添加 @file:Suppress("JAVA_MODULE_DOES_NOT_EXPORT_PACKAGE") ，
		 * 以解决编译报错：Symbol is declared in module 'jdk.compiler' which does not export package 'com.sun.tools.javac.code'
		 */
		val symbol = element as Symbol
		val owner = symbol.owner
		val className = owner.name

		val methodSymbol = symbol as Symbol.MethodSymbol
		val uiStateType = methodSymbol.params[0].type
		val uiStateList = "${uiStateType.toString().lastName().toLowerCaseInFirst()}_List"

		FileSpec.builder("$generateFilePackageName.$PACKAGE_LAUNCHER.$PACKAGE_SCREENSHOT", "ScreenShotAllStateOf${className}")
			.addImportList(uiStateList)
			.addFunction(generateScreenShotAllStateFunSpec(uiStateList))
			.addFunction(
				generateScreenShotFunSpec(
					uiStateType.asTypeName(),
					symbol.owner.qualifiedName.toString(),
					element.qualifiedName.toString()
				)
			)
			.build()
			.writeTo(dir) // 写入文件
	}

	private fun FileSpec.Builder.addImportList(uiStateList: String): FileSpec.Builder {
		return this.addImport(generateFilePackageName, uiStateList)
			.addImport(generateFilePackageName, R)
			.addImport("android.content", INTENT)
			.addImport("android.widget", FRAMELAYOUT)
			.addImport("android.widget", RELATIVELAYOUT)
			.addImport("${generateFilePackageName}.$PACKAGE_BITMAP", GETBITMAP)
			.addImport("${generateFilePackageName}.$PACKAGE_BITMAP", COMPRESSTOFILE)
			.addImport("kotlinx.coroutines", DELAY)
			.addImport("${generateFilePackageName}.$PACKAGE_FILE", GETUITESTDIRECTORY)
	}

	private fun generateScreenShotAllStateFunSpec(uiStateList: String): FunSpec {
		return FunSpec.builder("screenShotAllState")
			.addModifiers(KModifier.INTERNAL, KModifier.SUSPEND)
			.addParameterOfLauncher()
			.addStatement(generateFormatOfScreenShotAllState(uiStateList))
			.build()
	}

	private fun generateFormatOfScreenShotAllState(uiStateList: String): String {
		return "${uiStateList}.forEachIndexed { $INDEX, $UI_STATE ->\n" +
				"	$FUN_NAME_SCREENSHOT($LAUNCHER, $UI_STATE, $INDEX)\n" +
				"}"
	}

	private fun generateScreenShotFunSpec(
		uiStateTypeName: TypeName,
		activityQualifiedName: String,
		updateViewFun: String
	): FunSpec {
		return FunSpec.builder(FUN_NAME_SCREENSHOT)
			.addModifiers(KModifier.INTERNAL, KModifier.SUSPEND)
			.addParameterOfLauncher()
			.addParameter(UI_STATE, uiStateTypeName)
			.addParameter(INDEX, Int::class.java)
			.addStatement(generateFormatOfScreenShot(activityQualifiedName, updateViewFun))
			.build()
	}

	private fun generateFormatOfScreenShot(
		activityQualifiedName: String,
		updateViewFun: String
	): String {
		val activityQualifiedNamePlus = activityQualifiedName.replace(".", "_")
		return "$VAL $ROOT_VIEW = $LAUNCHER.findViewById<$FRAMELAYOUT>($R.id.root_view)\n" +
				"$ROOT_VIEW.removeAllViews()\n" +
				"$VAL $VIEW = $LAUNCHER.localActivityManager.startActivity(\n" +
				"    $activityQualifiedName::$CLASS_JAVA.canonicalName,\n" +
				"    $INTENT($LAUNCHER, $activityQualifiedName::$CLASS_JAVA)\n" +
				").decorView\n" +
				"($LAUNCHER.localActivityManager.currentActivity $AS $activityQualifiedName).$updateViewFun($UI_STATE)\n" +
				"\n" +
				"$ROOT_VIEW.addView(\n" +
				"    $VIEW, 0, $RELATIVELAYOUT.LayoutParams(\n" +
				"        $RELATIVELAYOUT.LayoutParams.MATCH_PARENT,\n" +
				"        $RELATIVELAYOUT.LayoutParams.MATCH_PARENT\n" +
				"    )\n" +
				")\n" +
				"\n" +
				"$ROOT_VIEW.$POST {\n" +
				"    $VAL $BITMAP = $ROOT_VIEW.$GETBITMAP() ?: $RETURN@$POST\n" +
				"    $VAL $DIR = $GETUITESTDIRECTORY()\n" +
				"    $BITMAP.$COMPRESSTOFILE(\n" +
				"        $DIR.absolutePath + \"/${activityQualifiedNamePlus}_screenShot_$${INDEX}$JPG\"\n" +
				"    )\n" +
				"}\n" +
				"$DELAY(1000)"
	}

	private fun FunSpec.Builder.addParameterOfLauncher(): FunSpec.Builder {
		return addParameter(
			LAUNCHER,
			ClassName.bestGuess("${generateFilePackageName}.$PACKAGE_LAUNCHER.$CLASS_LAUNCHER")
		)
	}

}
