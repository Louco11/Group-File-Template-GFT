package tools

import model.FileTemplate
import java.io.File
import java.nio.charset.Charset

object CreateTemplate  {

    fun createFileTemplate (
        pathChoose: String,
        pathTemplate: String,
        map: Map<String, String>,
        fileTemplateModel: FileTemplate
    ) {
        val nameFile = fileTemplateModel.name.replaceTemplate(map)
        val fileTemplate = File("$pathTemplate", fileTemplateModel.fileTemplatePath)
        val filePath = create(pathChoose, fileTemplateModel.path)
        val fileTemplateInPath =  File(filePath.path, nameFile)
        fileTemplateInPath.createNewFile()
        fileTemplateInPath.writeText(fileTemplate.readText(Charset.defaultCharset()).replaceTemplate(map))
    }

    private fun create(pathProject: String, pathFile: String): File {
        var pathStep = pathProject
        pathFile.split("/").forEach {
            val file = File(pathStep, it)
            if (!file.isDirectory) file.mkdir()
            pathStep = "$pathStep/$it"
        }
        return File(pathStep)
    }
}