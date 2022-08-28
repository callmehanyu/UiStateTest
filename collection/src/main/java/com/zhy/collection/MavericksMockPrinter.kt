package com.zhy.collection

import android.content.Context
import android.util.Log
import androidx.annotation.WorkerThread
import com.zhy.collection.uistate.UiState
import com.zhy.collection.util.TodoException
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
 */
@WorkerThread
fun <T : UiState> writeMock(
    context: Context,
    objectToMock: T,
) {
    val pageName = objectToMock::class.simpleName
    if (pageName.isNullOrBlank()) {
        throw IllegalArgumentException("pageName 不能为空")
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
 */
@WorkerThread
private fun <T : UiState> printMockFile(
    context: Context,
    instanceToMock: T,
) {

//    /**
//     * equalsUnique
//     */
//    if (!needCollect(instanceToMock, cacheMockList)) {
//        Log.d("needCollect", "illegal state")
//        return
//    }

    /**
     * todo 注解 package import index
     */
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

/**
 * 1.检测是否相同
 * 2.检测是否有todo
 */
private fun<T : UiState> needCollect(
    instanceToMock: T,
    cacheMockList: List<T>,
): Boolean {
    return try {
        Log.d("needCollect", "contains=${cacheMockList.any { it.equalsUnique(instanceToMock) }}")
        cacheMockList.any { it.equalsUnique(instanceToMock) }
    } catch (e: Exception) {
        Log.d("needCollect", "TodoException")
        true
    }
}