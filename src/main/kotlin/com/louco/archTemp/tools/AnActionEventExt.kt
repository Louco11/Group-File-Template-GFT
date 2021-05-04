package com.louco.archTemp.tools

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.louco.archTemp.constant.Constants
import com.louco.archTemp.mapper.JsonModelMapper
import com.louco.archTemp.model.MainClassJson
import java.io.File
import java.nio.charset.Charset

fun AnActionEvent.getListTemplate(): List<MainClassJson> {
    val listTemplate = mutableListOf<MainClassJson>()

    this.getData(CommonDataKeys.PROJECT)?.let {
        val basePath = "${it.basePath}${Constants.PATH_TEMPLATE}"
        val dirTemplate = File(basePath)
        if (dirTemplate.isDirectory) {
            dirTemplate.list().forEach { templatePath ->
                val pathMainFile = "$basePath/$templatePath"
                val mainFile = File(pathMainFile, Constants.MAIN_FILE_TEMPLATE)
                if (mainFile.isFile) {
                    try {
                        listTemplate.add(
                            JsonModelMapper.mapToMainClassXml(mainFile.readText(Charset.defaultCharset()))
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
