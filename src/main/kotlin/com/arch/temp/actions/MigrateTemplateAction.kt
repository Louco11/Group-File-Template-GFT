package com.arch.temp.actions

import com.arch.temp.constant.Constants
import com.arch.temp.constant.Constants.MAIN_FILE_TEMPLATE
import com.arch.temp.constant.Constants.MAIN_SHORT_FILE_TEMPLATE
import com.arch.temp.mapper.JsonModelMapper
import com.arch.temp.tools.FileTemplateExt.getRootPathTemplate
import com.arch.temp.tools.getBasePathTemplate
import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.ui.Messages
import org.jetbrains.annotations.NonNls
import java.io.File
import java.nio.charset.Charset

class MigrateTemplateAction : AnAction() {

    override fun actionPerformed(actionEvent: AnActionEvent) {

        val pathTemplateSelect = actionEvent.getData(CommonDataKeys.VIRTUAL_FILE)
        val migratePathTemplate = if (pathTemplateSelect?.parent?.path == getRootPathTemplate()) {
            actionEvent.getBasePathTemplate()
        } else {
            getRootPathTemplate()
        }
        val nameTemplate = Messages.showInputDialog(
            "Do you want to rename?",
            "Rename",
            null,
            pathTemplateSelect?.name,
            null
        ) ?: return

        val fileIde = File("$migratePathTemplate/$nameTemplate").apply {
            if (!isDirectory) mkdirs()
        }

        pathTemplateSelect?.path?.let { path ->
            migrateTemplate(path, fileIde)
        }

        if (pathTemplateSelect?.name != nameTemplate) {
            renameMainFile(fileIde, nameTemplate)
        }
    }

    private fun renameMainFile(fileIde: File, nameTemplate: String) {
        fileIde.listFiles()
            ?.firstOrNull {
                it.name == MAIN_FILE_TEMPLATE
                        || it.name == MAIN_SHORT_FILE_TEMPLATE
            }?.let {
                val mainJson = JsonModelMapper.mapToMainClass(it.readText(Charset.defaultCharset()))
                val jsonParse = JsonModelMapper.mapToString(mainJson.copy(name = nameTemplate))
                it.writeText(jsonParse)
            }
    }

    private fun migrateTemplate(
        pathTemplateSelect: @NonNls String,
        fileIde: File
    ) {
        try {
            if (templateText == Constants.ACTION_MIGRATE) {
                File(pathTemplateSelect).renameTo(fileIde)
            } else {
                File(pathTemplateSelect).copyRecursively(fileIde)
            }
        } catch (_: SecurityException) {
            Messages.showDialog(
                "Denies write access to either the old or new path names",
                "Error",
                listOf("OK").toTypedArray(),
                0,
                Messages.getWarningIcon()
            )
        }
    }

    override fun getActionUpdateThread(): ActionUpdateThread {
        return ActionUpdateThread.BGT
    }

    override fun update(e: AnActionEvent) {
        val project = e.project
        val pathTemplate = e.getData(CommonDataKeys.VIRTUAL_FILE)?.path
        val shortMainTemplate = File("$pathTemplate/$MAIN_SHORT_FILE_TEMPLATE")
        val shortTemplate = File("$pathTemplate/$MAIN_FILE_TEMPLATE")
        e.presentation.isEnabledAndVisible = project != null && (shortMainTemplate.isFile || shortTemplate.isFile)
    }

}