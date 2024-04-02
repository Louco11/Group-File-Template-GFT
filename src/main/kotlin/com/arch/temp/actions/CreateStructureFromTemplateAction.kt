package com.arch.temp.actions

import com.arch.temp.constant.Constants.TagXml.DEFAULT_TAG_DAY
import com.arch.temp.constant.Constants.TagXml.DEFAULT_TAG_FULL_PACKAGE
import com.arch.temp.constant.Constants.TagXml.DEFAULT_TAG_MAIN_PACKAGE
import com.arch.temp.constant.Constants.TagXml.DEFAULT_TAG_MONTH
import com.arch.temp.constant.Constants.TagXml.DEFAULT_TAG_TIME
import com.arch.temp.constant.Constants.TagXml.DEFAULT_TAG_YEAR
import com.arch.temp.model.MainClassJson
import com.arch.temp.tools.CreateTemplate
import com.arch.temp.tools.getPackFromBuildGradleFile
import com.arch.temp.tools.getPackFromManifest
import com.arch.temp.tools.getPackage
import com.arch.temp.view.CreateTemplateDialog
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import java.text.SimpleDateFormat
import java.util.*

class CreateStructureFromTemplateAction(private val mainClass: MainClassJson) :
    AnAction(mainClass.name, mainClass.description, null) {

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
        val mapParam = inputMap.toMutableMap()
        createDefaultTag(mapParam, event)

        mainClass.fileTemplate.forEach { fileTemplateModel ->
            CreateTemplate.createFileTemplate(
                event.project!!,
                event.getData(CommonDataKeys.VIRTUAL_FILE)?.path.orEmpty(),
                mainClass.globalBasePath,
                mapParam,
                fileTemplateModel
            )
        }
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