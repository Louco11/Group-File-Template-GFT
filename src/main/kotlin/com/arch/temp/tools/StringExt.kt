package com.arch.temp.tools

const val COUNT_PATH = 3

fun String.replaceTemplate(map: Map<String, String>): String {
    var stringTemp = this
    map.keys.forEach { key ->
        stringTemp = stringTemp.replace("{$key}[-C]", map[key]!!.snakeToUpperCamelCase())
        stringTemp = stringTemp.replace("{$key}[-c]", map[key]!!.snakeToLowerCamelCase())
        stringTemp = stringTemp.replace("{$key}[-s]", map[key]!!.camelToSnakeCase())
        stringTemp = stringTemp.replace("{$key}", map[key]!!)
    }
    return stringTemp
}

fun String.toTmFile() = "$this.tm"

// snakeCase or SnakeCase to snake_case
fun String.camelToSnakeCase(): String {
    val camelRegex = "(?<=[a-zA-Z])[A-Z]".toRegex()
    return camelRegex.replace(this) { "_${it.value}" }.lowercase()
}

/* snake_case to snakeCase */
fun String.snakeToLowerCamelCase(): String {
    val snakeRegex = "_[a-zA-Z]".toRegex()
    return snakeRegex.replace(this) {
        it.value.replace("_", "").uppercase()
    }.replaceFirstChar { it.lowercase() }
}

/* snake_case to SnakeCase */
fun String.snakeToUpperCamelCase(): String {
    return this.snakeToLowerCamelCase().replaceFirstChar { it.uppercase() }
}

fun String.getMainPackage(): String {
    val listPath = this.split(".")
    return if (listPath.size == COUNT_PATH) {
        this
    } else {
        "${listPath[0]}.${listPath[1]}.${listPath[2]}"
    }
}