package com.arch.temp.tools

fun String.replaceTemplate(map: Map<String, String>): String {
    var stringTemp = this
    map.keys.forEach { key ->
        stringTemp = stringTemp.replace("{$key}[-C]", map[key]!!.snakeToUpperCamelCase())
            .replace("{$key}[-c]", map[key]!!.snakeToLowerCamelCase())
            .replace("{$key}[-s]", map[key]!!.camelToSnakeCase())
            .replace("{$key}[-p]", map[key]!!.pointBetweenWords())
            .replace("{$key}[-sl]", map[key]!!.slashBetweenWords())
            .replace("{$key}[-d]", map[key]!!.slashDashWords())
            .replace("{$key}", map[key]!!)
    }
    return stringTemp
}

fun String.toTmFile() = "$this.tm"

// defise-case or point.case to wordCase or WordCase
fun String.toCamelCase(): String {
    val slashRegex = "-[a-zA-Z]".toRegex()
    val pointRegex = "\\.[a-zA-Z]".toRegex()
    val firstString = slashRegex.replace(this) {it.value.replace("-", "").uppercase()}
    return pointRegex.replace(firstString) { it.value.replace(".", "").uppercase()}
}

// wordCase or WordCase to word_case
fun String.camelToSnakeCase(): String {
    val camelRegex = "(?<=[a-zA-Z])[A-Z]".toRegex()
    return camelRegex.replace(this.toCamelCase()) { "_${it.value}" }.lowercase()
}

// wordCase or WordCase or word_case to word.case
fun String.pointBetweenWords(): String {
    return this.camelToSnakeCase().replace("_",".")
}

// wordCase or WordCase or word_case to word/case
fun String.slashBetweenWords(): String {
    return this.camelToSnakeCase().replace("_","/")
}

// wordCase or WordCase or word_case to word-case
fun String.slashDashWords(): String {
    return this.camelToSnakeCase().replace("_","-")
}

// word_case to wordCase
fun String.snakeToLowerCamelCase(): String {
    val snakeRegex = "_[a-zA-Z]".toRegex()
    return snakeRegex.replace(this.toCamelCase()) {
        it.value.replace("_", "").uppercase()
    }.replaceFirstChar { it.lowercase() }
}

// word_case to WordCase
fun String.snakeToUpperCamelCase(): String {
    return this.snakeToLowerCamelCase().replaceFirstChar { it.uppercase() }
}