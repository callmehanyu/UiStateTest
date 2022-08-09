package com.zhy.collection.printer

import android.util.Log
import java.lang.StringBuilder

fun saveMockUiStateToLocal() {
    val instanceToMock = Class.forName("com.example.mvi.printer.mock.Test2ActivityMockKt")
    instanceToMock.declaredFields.forEach {

        val code = ConstructorCodeGenerator(
            it,
            200,
            200,
            MockableMavericks.mockPrinterConfiguration.customTypePrinters
        )

        val sb = StringBuilder()
            .append("package ${MockableMavericks.mockPrinterConfiguration.mockPackage(it)}\n")
        code.imports.forEach { import ->
            sb.append("import $import\n")
        }
        sb.append("\n").append(code.lazyPropertyToCreateObject)

        Log.d("RESULTS_TAG", sb.toString())
    }


}