package com.arch.temp.actions

import com.arch.temp.constant.Constants.ANDROID_MANIFEST_FILE
import com.arch.temp.constant.Constants.CreatePackage.MAIN_PATH
import com.arch.temp.constant.Constants.TagXml.DEFAULT_TAG_DAY
import com.arch.temp.constant.Constants.TagXml.DEFAULT_TAG_FULL_PACKAGE
import com.arch.temp.constant.Constants.TagXml.DEFAULT_TAG_MAIN_PACKAGE
import com.arch.temp.constant.Constants.TagXml.DEFAULT_TAG_MONTH
import com.arch.temp.constant.Constants.TagXml.DEFAULT_TAG_TIME
import com.arch.temp.constant.Constants.TagXml.DEFAULT_TAG_YEAR
import com.arch.temp.model.MainClassJson
import com.arch.temp.tools.CreateTemplate
import com.arch.temp.tools.getPackage
import com.arch.temp.view.CreateTemplateDialog
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.vfs.VirtualFileManager
import com.intellij.psi.impl.PsiParserFacadeImpl
import java.io.File
import java.nio.charset.Charset
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern

const val REX = "package=\"+[A-Za-z0-9.]+\""

class DialogCreateArchitectureAction(private val mainClass: MainClassJson) :
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
        PsiParserFacadeImpl(event.project!!)
        createDefaultTag(mapParam, event)
        val basePath = event.getData(CommonDataKeys.PROJECT)?.basePath.orEmpty()
        val pathCreate = "$basePath${mainClass.path}"
        mainClass.fileTemplate.forEach { file ->
            CreateTemplate.createFileTemplate(
                basePath,
                event.getData(CommonDataKeys.VIRTUAL_FILE)?.path.orEmpty(),
                pathCreate,
                mapParam,
                file
            )
        }
        VirtualFileManager.getInstance().asyncRefresh {
            VirtualFileManager.getInstance().syncRefresh()
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

        try {
            val path = event.getData(CommonDataKeys.VIRTUAL_FILE)?.path.orEmpty()
            val mainPath = path.removeRange(path.indexOf(MAIN_PATH) + MAIN_PATH.length + 1, path.length)

            val androidManifest = File(mainPath, ANDROID_MANIFEST_FILE)
            if (androidManifest.isFile) {
                val text = androidManifest.readText(Charset.defaultCharset())
                val pattern = Pattern.compile(REX)
                val match = pattern.matcher(text)
                if (match.find()) {
                    mapParam[DEFAULT_TAG_MAIN_PACKAGE] = match.group()
                            .replace("package=\"", "")
                            .replace("\"", "")
                } else {
                    mapParam[DEFAULT_TAG_MAIN_PACKAGE] = ""
                }
            }
        } catch (e: Exception) {
            mapParam[DEFAULT_TAG_MAIN_PACKAGE] = ""
        }
    }
}