package com.arch.temp.constant

object Constants {

    const val PATH_TEMPLATE = "/templates"
    const val EMPTY_TEMPLATE_PATH_NAME = "EmptyTemplate"
    const val MAIN_FILE_TEMPLATE = "main.json"

    object TagXml {
        const val FIELD_NAME = "name"
        const val FIELD_DESCRIPTION = "description"
        const val FIELD_PATH = "path"
        const val FIELD_PARAMETERS = "param"
        const val FIELD_ADD_FILE = "addFile"
        const val DEFAULT_TAG_FULL_PACKAGE = "package"
        const val DEFAULT_TAG_MAIN_PACKAGE = "pack"
    }

    object CreatePackage {
        const val MAIN_JAVA = "java"
        const val MAIN_KOTLIN = "kotlin"
        const val ANDROID_RES = "res"
    }

}