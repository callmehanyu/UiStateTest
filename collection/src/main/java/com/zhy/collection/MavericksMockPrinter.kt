package com.zhy.collection

import android.content.Context
import android.util.Log
import androidx.annotation.WorkerThread
import java.io.File
import java.lang.StringBuilder

/**
 * The command line tooling looks for output with these tags - so it is important that these are not changed
 * without also updating that script.
 */
internal const val RESULTS_TAG: String = "MOCK_PRINTER_RESULTS"
private const val ERROR_TAG = "MOCK_PRINTER_ERROR"
private const val INFO_TAG = "MOCK_PRINTER_INFO"

/**
 * 执行前，请检查是否更新了最新的uistate mock文件
 * todo 重复
 */
@WorkerThread
fun <T : Any> writeMock(
    context: Context,
    objectToMock: T,
    cacheMockList: List<T>,
) {
    val pageName = objectToMock::class.simpleName
    if (pageName.isNullOrBlank()) {
        throw IllegalArgumentException("pageName 不能为空")
        return
    }
    val objectName = objectToMock::class.simpleName

    // The reflection sometimes fails if unexpected state types are present
    @Suppress("Detekt.TooGenericExceptionCaught")
    try {
        Log.d(INFO_TAG, "Generating state for $objectName")
        printMockFile(
            context,
            objectToMock,
        )
    } catch (e: Throwable) {
        Log.e(ERROR_TAG, "Error creating mavericks mock code for $objectName", e)
    }
}

/**
 * Print out the code that is needed to construct the given object. This is useful for creating mock state or argument objects.
 * Use "adb logcat -s "MAVERICKS_STATE" -v raw -v color" in the terminal to visualize the output nicely.
 *
 * @param listTruncationThreshold If greater then 0, any lists found will be truncated to this number of items. If 0 or less, no truncation will occur.
 * @param stringTruncationThreshold If greater then 0, any Strings found will be truncated to this number or characters. If 0 or less, no truncation will occur.
 */
@WorkerThread
private fun <T : Any> printMockFile(
    context: Context,
    instanceToMock: T,
) {
    val code = ConstructorCodeGenerator(
        instanceToMock,
        300,
        3,
        MockableMavericks.mockPrinterConfiguration.customTypePrinters
    )

    val file = File(context.cacheDir, "${instanceToMock::class.simpleName}Collection.kt")

    val sb = StringBuilder()
        .append("package ${MockableMavericks.mockPrinterConfiguration.mockPackage(instanceToMock)}\n")
    code.imports.forEach {
        sb.append("import $it\n")
    }
    sb.append("\n").append(code.lazyPropertyToCreateObject)

    file.appendText(sb.toString())

    Log.d(RESULTS_TAG, file.canonicalPath)
}

//private fun<T : Any> checkIfIllagel(
//    context: Context,
//    instanceToMock: T,
//): Boolean {
//
//}