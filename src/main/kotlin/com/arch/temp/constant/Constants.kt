package com.arch.temp.constant

import java.io.File

object Constants {

    const val PATH_TEMPLATE = "/templates"
    const val TEMPLATE = "templates"
    const val EMPTY_TEMPLATE_PATH_NAME = "EmptyTemplate"
    const val EMPTY_SHORT_TEMPLATE_PATH_NAME = "EmptyShortTemplate"
    const val MAIN_FILE_TEMPLATE = "main.json"
    const val MAIN_SHORT_FILE_TEMPLATE = "main_short.json"
    const val ANDROID_MANIFEST_FILE = "AndroidManifest.xml"
    const val BUILD_GRADLE_FILE = "build.gradle.kts"
    const val ACTION_MIGRATE = "MigrateTemplateActionRemove"
    val SLASH = File.separatorChar

    object TagXml {
        const val FIELD_NAME = "name"
        const val FIELD_DESCRIPTION = "description"
        const val FIELD_PARAMETERS = "param"
        const val FIELD_SELECT_PARAMETERS = "selectParam"
        const val FIELD_ADD_FILE = "addFile"
        const val FIELD_INSERT_IN_FILE = "insertInFile"

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
        const val SRC_PATH = "src"
    }

    object ExtensionConst {
        const val ROOT_ID = "templates"
        const val GFT_ROOT_NAME = "Ide GFTemplates"
        const val GFT_PROJECT_ID = "gft_project"
        const val GFT_PROJECT_NAME = "Project GFTemplates"
    }

    enum class Options(val nameOption: String, val  example : String) {
        UPPER_CAMEL_CASE("[-C]", "ExampleName"),
        LOWER_CAMEL_CASE("[-c]", "exampleName"),
        SCREAMING_SNAKE_CASE("[-S]", "EXAMPLE_NAME"),
        SNAKE_CASE("[-s]", "example_name"),
        POINT_BETWEEN("[-p]", "example.name"),
        SLASH_BETWEEN("[-sl]", "example${SLASH}name"),
        DASH_BETWEEN("[-d]", "example-name"),
        LOWER_CASE("[-low]","examplename")
    }
}