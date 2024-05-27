package com.arch.temp.tools

import com.arch.temp.constant.Constants.Options
import com.arch.temp.constant.Constants.SLASH

fun String.replaceTemplate(map: Map<String, String>): String {
    var stringTemp = this
    map.keys.forEach { key ->
        stringTemp = stringTemp.replace("{$key}${Options.UPPER_CAMEL_CASE.nameOption}", map[key]!!.snakeToUpperCamelCase())
            .replace("{$key}${Options.LOWER_CAMEL_CASE.nameOption}", map[key]!!.snakeToLowerCamelCase())
            .replace("{$key}${Options.SCREAMING_SNAKE_CASE.nameOption}", map[key]!!.camelToScreamingSnakeCase())
            .replace("{$key}${Options.SNAKE_CASE.nameOption}", map[key]!!.camelToSnakeCase())
            .replace("{$key}${Options.POINT_BETWEEN.nameOption}", map[key]!!.pointBetweenWords())
            .replace("{$key}${Options.SLASH_BETWEEN.nameOption}", map[key]!!.slashBetweenWords())
            .replace("{$key}${Options.DASH_BETWEEN.nameOption}", map[key]!!.slashDashWords())
            .replace("{$key}${Options.LOWER_CASE.nameOption}", map[key]!!.lowWords())
            .replace("{$key}", map[key]!!)
    }
    return stringTemp
}

fun String.toTmFile() = "$this.tm"

// def ise-case or point.case to wordCase or WordCase
fun String.toCamelCase(): String {
    val slashRegex = "-[a-zA-Z]".toRegex()
    val pointRegex = "\\.[a-zA-Z]".toRegex()
    val spaceRegex = "\\s[a-zA-Z]".toRegex()
    val firstString = slashRegex.replace(this) {it.value.replace("-", "").uppercase()}
    val delPoint = pointRegex.replace(firstString) { it.value.replace(".", "").uppercase()}
    return spaceRegex.replace(delPoint) { it.value.replace(" ", "").uppercase()}
}

// wordCase or WordCase to word_case
fun String.camelToSnakeCase(): String {
    val camelRegex = "(?<=[a-zA-Z])[A-Z]".toRegex()
    return camelRegex.replace(this.toCamelCase()) { "_${it.value}" }.lowercase()
}

// wordCase or WordCase to WORD_CASE
fun String.camelToScreamingSnakeCase(): String {
    val camelRegex = "(?<=[a-zA-Z])[A-Z]".toRegex()
    return camelRegex.replace(this.toCamelCase()) { "_${it.value}" }.uppercase()
}

// to word.case
fun String.pointBetweenWords(): String {
    return this.camelToSnakeCase().replace("_",".")
}

// to wordcase
fun String.lowWords(): String {
    return this.camelToSnakeCase().replace("_","")
}

// to word/case
fun String.slashBetweenWords(): String {
    return this.camelToSnakeCase().replace("_", "$SLASH")
}

// to word-case
fun String.slashDashWords(): String {
    return this.camelToSnakeCase().replace("_","-")
}

// to wordCase
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