package com.arch.temp.constant

object Constants {

    const val PATH_TEMPLATE = "/templates"
    const val EMPTY_TEMPLATE_PATH_NAME = "EmptyTemplate"
    const val MAIN_FILE_TEMPLATE = "main.json"
    const val ANDROID_MANIFEST_FILE = "AndroidManifest.xml"

    object TagXml {
        const val FIELD_NAME = "name"
        const val FIELD_DESCRIPTION = "description"
        const val FIELD_PATH = "path"
        const val FIELD_PARAMETERS = "param"
        const val FIELD_SELECT_PARAMETERS = "selectParam"
        const val FIELD_ADD_FILE = "addFile"
        const val DEFAULT_TAG_FULL_PACKAGE = "package"
        const val DEFAULT_TAG_MAIN_PACKAGE = "pack"
        const val DEFAULT_TAG_YEAR = "year"
        const val DEFAULT_TAG_MONTH = "month"
        const val DEFAULT_TAG_DAY = "day"
        const val DEFAULT_TAG_TIME = "time"
    }

    object CreatePackage {
        const val MAIN_JAVA = "java"
        const val MAIN_KOTLIN = "kotlin"
        const val ANDROID_RES = "res"
        const val PATH_PROJECT = "~"
        const val PATH_TEST = "test"
        const val MAIN_PATH = "main"
    }
}