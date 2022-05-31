package com.arch.temp.model


import com.google.gson.annotations.SerializedName

data class SelectParam(
    @SerializedName("paramName")
    val paramName: String,
    @SerializedName("paramValue")
    val paramValue: List<String>
)