package com.arch.temp.tools

import com.arch.temp.constant.Constants
import com.arch.temp.model.FileTemplate
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
        val fileTemplatePath =  fileTemplateModel.getPath()

        val filePath = when (fileTemplatePath.split(SPLASH).first()) {
            Constants.CreatePackage.ANDROID_RES -> createPathRes(pathChoose, fileTemplatePath.replaceTemplate(map))
            Constants.CreatePackage.PATH_TEST -> createPathTest(pathChoose, fileTemplatePath.replaceTemplate(map))
            else -> create(pathChoose, fileTemplatePath.replaceTemplate(map))
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

    fun createPathTest(pathProject: String, pathFileTemplate: String): File {
        println(pathProject)
        println(pathFileTemplate)
        val pathTest = "${Constants.CreatePackage.PATH_TEST}/"
        val path = pathProject.replace(Constants.CreatePackage.MAIN_PATH, Constants.CreatePackage.PATH_TEST)
            .split(pathTest)

        val pathTemplate = "$pathTest${path.last()}$SPLASH" +
                pathFileTemplate.replace(Constants.CreatePackage.PATH_TEST, "")

        return create(path.first(), pathTemplate)
    }

    private fun create(pathProject: String, pathFileTemplate: String): File {
        println(pathProject)
        println(pathFileTemplate)
        var pathStep = pathProject
        pathFileTemplate.split(SPLASH).forEach {
            val file = File(pathStep, it)
            if (!file.isDirectory) file.mkdir()
            pathStep = "$pathStep/$it"
        }
        return File(pathStep)
    }
}
fun main() {
    val pathProject = "/Users/louco/IntelliJIDEAProjects/MyApplication/app/src/main/java/com/example/myapplication"
    val pathFileTemplate = "test/viewModel"
    println(CreateTemplate.createPathTest(pathProject, pathFileTemplate).path)
}
