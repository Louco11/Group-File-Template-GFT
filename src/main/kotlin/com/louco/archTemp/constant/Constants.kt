package com.louco.archTemp.constant

object Constants {

    const val PATH_TEMPLATE = "/template"
    const val EMPTY_TEMPLATE_PATH_NAME = "EmptyTemplate"
    const val MAIN_FILE_TEMPLATE = "main.json"

    object TagXml{
        const val HEAD_TAG = "template"
        const val FIELD_NAME = "name"
        const val FIELD_DESCRIPTION = "description"
        const val FIELD_PATH = "path"
        const val FIELD_PARAMETERS= "param"
        const val FIELD_ADD_FILE= "addFile"
        const val DEFAULT_TAG_PACKAGE= "package"
    }

    object CreatePackage {
        const val MAIN_JAVA = "java"
        const val MAIN_KOTLIN = "kotlin"
    }

}