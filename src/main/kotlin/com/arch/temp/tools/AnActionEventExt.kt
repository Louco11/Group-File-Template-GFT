package com.arch.temp.tools

import com.arch.temp.constant.Constants
import com.arch.temp.mapper.JsonModelMapper
import com.arch.temp.model.MainClassJson
import com.arch.temp.model.MainShortClassJson
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import java.io.File
import java.nio.charset.Charset
import java.util.regex.Pattern

private const val PACKAGE_REX = "package=\"+[A-Za-z0-9.]+\""
private const val NAMESPACE_REX = "namespace = \"+[A-Za-z0-9.]+\""

fun AnActionEvent.getListTemplate(): List<MainClassJson> {
    val listTemplate = mutableListOf<MainClassJson>()

    this.getData(CommonDataKeys.PROJECT)?.let {
        val basePath = "${it.basePath}${Constants.PATH_TEMPLATE}"
        val dirTemplate = File(basePath)
        if (dirTemplate.isDirectory) {
            dirTemplate.list()?.forEach { templatePath ->
                val pathMainFile = "$basePath/$templatePath"
                val mainFile = File(pathMainFile, Constants.MAIN_FILE_TEMPLATE)
                if (mainFile.isFile) {
                    try {
                        listTemplate.add(
                            JsonModelMapper.mapToMainClass(mainFile.readText(Charset.defaultCharset())).apply {
                                globalBasePath = pathMainFile
                            }
                        )
                    } catch (_: Exception) {
                    }
                }

            }
        }
    }

    return listTemplate
}

fun AnActionEvent.getListShortTemplate(): List<MainShortClassJson> {
    val listTemplate = mutableListOf<MainShortClassJson>()

    this.getData(CommonDataKeys.PROJECT)?.let {
        val basePath = "${it.basePath}${Constants.PATH_TEMPLATE}"
        val dirTemplate = File(basePath)
        if (dirTemplate.isDirectory) {
            dirTemplate.list()?.forEach { templatePath ->
                val pathMainFile = "$basePath/$templatePath"
                val mainFile = File(pathMainFile, Constants.MAIN_SHORT_FILE_TEMPLATE)
                if (mainFile.isFile) {
                    try {
                        listTemplate.add(
                            JsonModelMapper.mapToShortMainClass(mainFile.readText(Charset.defaultCharset())).apply {
                                globalBasePath = pathMainFile
                            }
                        )
                    } catch (e: Exception) {
                    }
                }

            }
        }
    }

    return listTemplate
}

fun AnActionEvent.getPackage(): String {
    val mainJava = Constants.CreatePackage.MAIN_JAVA
    val mainKotlin = Constants.CreatePackage.MAIN_KOTLIN
    val path = getData(CommonDataKeys.VIRTUAL_FILE)?.path.orEmpty()
    var packge = ""
    if (path.indexOf(mainJava) > 0) {
        packge = path.removeRange(0, path.indexOf(mainJava) + mainJava.length + 1)
    }
    if (path.indexOf(mainKotlin) > 0) {
        packge = path.removeRange(0, path.indexOf(mainKotlin) + mainKotlin.length + 1)
    }
    return packge.replace("/", ".")
}

fun AnActionEvent.getPackFromManifest(): String {
    var pack = ""
    try {
        val path = getData(CommonDataKeys.VIRTUAL_FILE)?.path.orEmpty()
        val mainPath = path.removeRange(
            path.indexOf(Constants.CreatePackage.MAIN_PATH) + Constants.CreatePackage.MAIN_PATH.length + 1,
            path.length
        )

        val androidManifest = File(mainPath, Constants.ANDROID_MANIFEST_FILE)
        if (androidManifest.isFile) {
            val text = androidManifest.readText(Charset.defaultCharset())
            val pattern = Pattern.compile(PACKAGE_REX)
            val match = pattern.matcher(text)
            pack = if (match.find()) {
                match.group()
                    .replace("package=\"", "")
                    .replace("\"", "")
            } else {
                ""
            }
        }
    } catch (e: Exception) {
        pack = ""
    }
    return pack
}

fun AnActionEvent.getPackFromBuildGradleFile(): String {
    var pack = ""
    try {
        val path = getData(CommonDataKeys.VIRTUAL_FILE)?.path.orEmpty()
        val mainPath = path.removeRange(
            path.indexOf(Constants.CreatePackage.SRC_PATH),
            path.length
        )

        val buildGradle = File(mainPath, Constants.BUILD_GRADLE_FILE)
        if (buildGradle.isFile) {
            val text = buildGradle.readText(Charset.defaultCharset())
            val pattern = Pattern.compile(NAMESPACE_REX)
            val match = pattern.matcher(text)
            pack = if (match.find()) {
                match.group()
                    .replace("namespace = \"", "")
                    .replace("\"", "")
            } else {
                ""
            }
        }
    } catch (e: Exception) {
        pack = ""
    }
    return pack
}
