package com.gray.runtime

class BindingData {
    companion object {

        @JvmStatic
        fun bind(any: Any) {
            val bindingName = any.javaClass.name + "Binding"
            print(bindingName)
            val clazz = Class.forName(bindingName)
            val invoke = clazz.getMethod("create").invoke(null)
            val fields = any.javaClass.fields
            fields.filter { it.type == invoke.javaClass }.forEach {
                it.isAccessible = true
                it.set(any, invoke)
            }
        }
    }
}