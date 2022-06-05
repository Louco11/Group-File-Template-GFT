package com.arch.temp.tools

fun String.replaceTemplate(map: Map<String, String>): String {
    var stringTemp = this
    map.keys.forEach { key ->
        stringTemp = stringTemp.replace("{$key}[-C]", map[key]!!.snakeToUpperCamelCase())
            .replace("{$key}[-c]", map[key]!!.snakeToLowerCamelCase())
            .replace("{$key}[-s]", map[key]!!.camelToSnakeCase())
            .replace("{$key}[-p]", map[key]!!.pointBetweenWords())
            .replace("{$key}[-sl]", map[key]!!.slashBetweenWords())
            .replace("{$key}", map[key]!!)
    }
    return stringTemp
}

fun String.toTmFile() = "$this.tm"

// snakeCase or SnakeCase to snake_case
fun String.camelToSnakeCase(): String {
    val camelRegex = "(?<=[a-zA-Z])[A-Z]".toRegex()
    return camelRegex.replace(this) { "_${it.value}" }.lowercase()
}

fun String.pointBetweenWords(): String {
    return this.camelToSnakeCase().replace("_",".")
}

fun String.slashBetweenWords(): String {
    return this.camelToSnakeCase().replace("_","/")
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