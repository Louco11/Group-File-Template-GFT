package com.arch.temp.actions

import com.arch.temp.constant.Constants
import com.arch.temp.constant.Constants.MAIN_FILE_TEMPLATE
import com.arch.temp.constant.Constants.MAIN_SHORT_FILE_TEMPLATE
import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.application.PathManager
import com.intellij.openapi.wm.ToolWindowManager
import com.intellij.util.ui.JBUI.CurrentTheme.ToolWindow
import java.io.File

class MigrateTemplateAction : AnAction() {

    override fun actionPerformed(actionEvent: AnActionEvent) {
        val pathTemplateSelect = actionEvent.getData(CommonDataKeys.VIRTUAL_FILE)
        val newHomePathTemplate = "${PathManager.getHomePath()}${Constants.PATH_TEMPLATE}/${pathTemplateSelect?.name}"
        val fileIde = File(newHomePathTemplate)
        if (!fileIde.isDirectory) {
            fileIde.mkdirs()
        }
        pathTemplateSelect?.path?.let {
            File(it).renameTo(fileIde)
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