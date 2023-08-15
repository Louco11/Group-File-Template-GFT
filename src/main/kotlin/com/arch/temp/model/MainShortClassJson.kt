package com.arch.temp.model

import com.google.gson.annotations.SerializedName
import com.arch.temp.constant.Constants

data class MainShortClassJson (
    @SerializedName(Constants.TagXml.FIELD_NAME)
    val name: String = "No Name",
    @SerializedName(Constants.TagXml.FIELD_DESCRIPTION)
    val description: String = "Empty Template Description",
    @SerializedName(Constants.TagXml.FIELD_PATH)
    val path: String = "",
    @SerializedName(Constants.TagXml.FIELD_ADD_FILE)
    val fileTemplate: List<FileShortTemplate> = listOf()
) {
    var globalBasePath: String = ""
}