package com.louco.archTemp.tools

import com.louco.archTemp.constant.Constants
import com.louco.archTemp.model.FileTemplate
import java.io.File
import java.nio.charset.Charset

private const val SPLASH = "/"

object CreateTemplate  {

    fun createFileTemplate (
        pathChoose: String,
        pathTemplate: String,
        map: Map<String, String>,
        fileTemplateModel: FileTemplate
    ) {
        val nameFile = fileTemplateModel.name.replaceTemplate(map)
        val fileTemplate = File("$pathTemplate", fileTemplateModel.fileTemplatePath)

        val filePath = if (fileTemplateModel.path.split(SPLASH).first() == Constants.CreatePackage.ANDROID_RES) {
            createPathRes(pathChoose, fileTemplateModel.path.replaceTemplate(map))
        } else {
            create(pathChoose, fileTemplateModel.path.replaceTemplate(map))
        }
        val fileTemplateInPath =  File(filePath.path, nameFile)
        fileTemplateInPath.createNewFile()
        fileTemplateInPath.writeText(fileTemplate.readText(Charset.defaultCharset()).replaceTemplate(map))
    }

    private fun createPathRes(pathProject: String, pathFileTemplate: String): File {
        val mainJava = Constants.CreatePackage.MAIN_JAVA
        val mainKotlin = Constants.CreatePackage.MAIN_KOTLIN
        var packge = ""
        if (pathProject.indexOf(mainJava) > 0) {
            packge = pathProject.removeRange(pathProject.indexOf(mainJava), pathProject.length)
        }
        if (pathProject.indexOf(mainKotlin) > 0) {
            packge = pathProject.removeRange(pathProject.indexOf(mainKotlin), pathProject.length)
        }
        return create(packge, pathFileTemplate)
    }

    private fun create(pathProject: String, pathFileTemplate: String): File {
        var pathStep = pathProject
        pathFileTemplate.split(SPLASH).forEach {
            val file = File(pathStep, it)
            if (!file.isDirectory) file.mkdir()
            pathStep = "$pathStep/$it"
        }
        return File(pathStep)
    }
}