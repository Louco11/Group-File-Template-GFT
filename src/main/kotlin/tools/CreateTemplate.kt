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
        val pathCreate = "$pathChoose${fileTemplateModel.path}"
        val fileTemplate = File("$pathTemplate", fileTemplateModel.fileTemplatePath)
        val filePath = File(pathCreate)
        if (!filePath.isDirectory) filePath.mkdir()
        val fileTemplateInPath =  File(filePath.path, nameFile)
        fileTemplateInPath.createNewFile()
        fileTemplateInPath.writeText(fileTemplate.readText(Charset.defaultCharset()).replaceTemplate(map))
    }
}