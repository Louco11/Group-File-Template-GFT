package com.arch.temp.model

import com.google.gson.annotations.SerializedName
import com.arch.temp.constant.Constants
import com.google.gson.annotations.Expose

data class MainClassJson (
    @SerializedName(Constants.TagXml.FIELD_NAME)
    val name: String = "No Name",
    @SerializedName(Constants.TagXml.FIELD_DESCRIPTION)
    val description: String = "Empty Template Description",
    @SerializedName(Constants.TagXml.FIELD_PATH)
    val path: String = "",
    @SerializedName(Constants.TagXml.FIELD_PARAMETERS)
    val param: List<String> = listOf(),
    @SerializedName(Constants.TagXml.FIELD_ADD_FILE)
    val fileTemplate: List<FileTemplate> = listOf()
) {
    var globalBasePath: String = ""
}