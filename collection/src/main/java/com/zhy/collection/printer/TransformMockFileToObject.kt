package com.zhy.collection.printer

import kotlin.reflect.KMutableProperty1
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor

/**
 * string -> kclass
 */
fun to() {

}


/**
 * 将文件中的uistate转换成list
 * kclass -> ins
 */
fun<T : Any> transformMockFileToObject(serializedString: String, objectToMock: T): List<T> {

    val kClass = objectToMock::class
    val myK = kClass.primaryConstructor?.call() ?: return emptyList()
    kClass.memberProperties.forEach {  p ->
        if (p.name == "state") {
            (p as? KMutableProperty1<T, Any>)?.let { mp ->
                mp.set(myK, "state1")
            }
        }
    }

    return emptyList()
}

