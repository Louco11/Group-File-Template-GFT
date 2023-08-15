package com.arch.temp.mapper

import com.arch.temp.model.MainClassJson
import com.arch.temp.model.MainShortClassJson
import com.google.gson.GsonBuilder

object JsonModelMapper {

    fun mapToString(main: MainShortClassJson) : String {
        val gson = GsonBuilder().setPrettyPrinting().create()
        return gson.toJson(main)
    }
    fun mapToString(main: MainClassJson): String {
        val gson = GsonBuilder().setPrettyPrinting().create()
        return gson.toJson(main)
    }

    fun mapToMainClass(main: String) = GsonBuilder().create().fromJson(main, MainClassJson::class.java)
    fun mapToShortMainClass(main: String) = GsonBuilder().create().fromJson(main, MainShortClassJson::class.java)
}