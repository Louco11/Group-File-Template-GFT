package com.arch.temp.mapper

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.arch.temp.model.MainClassJson
import com.arch.temp.model.MainShortClassJson

object JsonModelMapper {

    fun mapToString(main: MainClassJson) = Gson().toJson(main)
    fun mapToString(main: MainShortClassJson) = Gson().toJson(main)

    fun mapToMainClass(main: String) = GsonBuilder().create().fromJson(main, MainClassJson::class.java)
    fun mapToShortMainClass(main: String) = GsonBuilder().create().fromJson(main, MainShortClassJson::class.java)
}