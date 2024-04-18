package com.arch.temp.actions

import com.arch.temp.constant.Constants.TagXml.DEFAULT_TAG_DAY
import com.arch.temp.constant.Constants.TagXml.DEFAULT_TAG_FULL_PACKAGE
import com.arch.temp.constant.Constants.TagXml.DEFAULT_TAG_MAIN_PACKAGE
import com.arch.temp.constant.Constants.TagXml.DEFAULT_TAG_MONTH
import com.arch.temp.constant.Constants.TagXml.DEFAULT_TAG_TIME
import com.arch.temp.constant.Constants.TagXml.DEFAULT_TAG_YEAR
import com.arch.temp.model.InsertInFileTemplateModel
import com.arch.temp.model.MainClassJson
import com.arch.temp.tools.*
import com.arch.temp.view.CreateTemplateDialog
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class CreateStructureFromTemplateAction(private val mainClass: MainClassJson) :
    AnAction(mainClass.name, mainClass.description, null) {
    private val SPLASH = File.separatorChar

    override fun actionPerformed(event: AnActionEvent) {
        if (mainClass.param.isEmpty() && mainClass.selectParam.isEmpty()) {
            createTemplate(mapOf(), event)
        } else {
            CreateTemplateDialog(event.project, mainClass) { inputMap ->
                createTemplate(inputMap, event)
            }.showAndGet()
        }
    }

    private fun createTemplate(
        inputMap: Map<String, String>,
        event: AnActionEvent
    ) {
        val project = event.project ?: return

        val mapParam = inputMap.toMutableMap()
        createDefaultTag(mapParam, event)

        mainClass.fileTemplate.forEach { fileTemplateModel ->
            CreateTemplate.createFileTemplate(
                project,
                event.getData(CommonDataKeys.VIRTUAL_FILE)?.path.orEmpty(),
                mainClass.globalBasePath,
                mapParam,
                fileTemplateModel
            )
        }

        mainClass.insertInFileTemplate.forEach { template ->
            insertInFileFromTemplate(template, project, mapParam)
        }
    }

    private fun insertInFileFromTemplate(
        template: InsertInFileTemplateModel,
        project: Project,
        mapParam: MutableMap<String, String>
    ) {
        val replacePath = template.path.replaceTemplate(mapParam)
        val insertFilePath = if (template.path.first() == SPLASH) replacePath else "$SPLASH${replacePath}"

        ApplicationManager.getApplication().runWriteAction {
            val file = File("${project.basePath}$insertFilePath")
            if (!file.isFile) {
                showError("In path $insertFilePath not found")
                return@runWriteAction
            }
            val buffer = StringBuilder()
            val temp = File(mainClass.globalBasePath, template.fileTemplatePath)
            if (!temp.isFile) {
                showError("In fileTemplatePath ${template.fileTemplatePath} not found")
                return@runWriteAction
            }
            if (template.line == 0) {
                buffer.appendLine(temp.readText().replaceTemplate(mapParam))
            }
            val lines = file.readLines()
            lines.forEachIndexed { index, s ->
                if (index == (template.line - 1)) {
                    buffer.appendLine(temp.readText().replaceTemplate(mapParam))
                }
                buffer.appendLine(s)
            }

            if (template.line < 0 || template.line > lines.size) {
                buffer.appendLine(temp.readText().replaceTemplate(mapParam))
            }

            file.writeText(buffer.toString().trimEnd())
        }
    }

    private fun showError(message: String) {
        Messages.showDialog(
            message,
            "Error",
            listOf("OK").toTypedArray(),
            0,
            Messages.getWarningIcon()
        )
    }

    private fun createDefaultTag(
        mapParam: MutableMap<String, String>,
        event: AnActionEvent
    ) {
        val time = SimpleDateFormat("hh:mm")
        val day = SimpleDateFormat("dd")
        val year = SimpleDateFormat("yyyy")
        val month = SimpleDateFormat("MM")

        val currentTime = time.format(Date())
        val currentDay = day.format(Date())
        val currentYear = year.format(Date())
        val currentMonth = month.format(Date())

        mapParam[DEFAULT_TAG_TIME] = currentTime
        mapParam[DEFAULT_TAG_DAY] = currentDay
        mapParam[DEFAULT_TAG_MONTH] = currentMonth
        mapParam[DEFAULT_TAG_YEAR] = currentYear

        val valuePackage = event.getPackage()
        mapParam[DEFAULT_TAG_FULL_PACKAGE] = valuePackage

        val valuePack = event.getPackFromManifest().ifEmpty { event.getPackFromBuildGradleFile() }
        mapParam[DEFAULT_TAG_MAIN_PACKAGE] = valuePack
    }
}