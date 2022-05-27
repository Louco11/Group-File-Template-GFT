package com.arch.temp.tools

const val COUNT_PATH = 3

fun String.replaceTemplate(map: Map<String, String>): String {
    var stringTemp = this
    map.keys.forEach { key ->
        stringTemp = stringTemp.replace("{$key}", map[key]!!)
        stringTemp = stringTemp.replace("[$key]", map[key]!!.camelToSnakeCase())
    }
    return stringTemp
}

fun String.toTmFile() = "$this.tm"

// String extensions
fun String.camelToSnakeCase(): String {
    val camelRegex = "(?<=[a-zA-Z])[A-Z]".toRegex()
    return camelRegex.replace(this) { "_${it.value}" }.lowercase()
}

fun String.getMainPackage(): String {
    val listPath = this.split(".")
    return if (listPath.size == COUNT_PATH) {
        this
    } else {
        "${listPath[0]}.${listPath[1]}.${listPath[2]}"
    }
}