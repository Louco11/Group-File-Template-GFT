package tools

import actions.DialogCreateArchitectureAction
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.project.impl.ProjectExImpl
import constant.Constants
import mapper.JsonModelMapper
import model.MainClassJson
import java.io.File
import java.nio.charset.Charset

fun AnActionEvent.getListTemplate(): List<MainClassJson> {
    val listTemplate = mutableListOf<MainClassJson>()

    this.getData(CommonDataKeys.PROJECT)?.let {
        val basePath = "${(it as ProjectExImpl).basePath}${Constants.PATH_TEMPLATE}"
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