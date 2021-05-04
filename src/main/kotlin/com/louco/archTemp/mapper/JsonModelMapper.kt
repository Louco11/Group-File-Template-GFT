package com.louco.archTemp.mapper

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.louco.archTemp.model.MainClassJson

object JsonModelMapper {

    fun mapToString(main: MainClassJson) = Gson().toJson(main)

    fun mapToMainClass(main: String) = GsonBuilder().create().fromJson(main, MainClassJson::class.java)
}