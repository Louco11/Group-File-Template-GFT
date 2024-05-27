package com.arch.temp.actions

import com.arch.temp.constant.Constants
import com.arch.temp.constant.Constants.MAIN_FILE_TEMPLATE
import com.arch.temp.constant.Constants.MAIN_SHORT_FILE_TEMPLATE
import com.arch.temp.constant.Constants.SPLASH
import com.arch.temp.mapper.JsonModelMapper
import com.arch.temp.model.MainClassJson
import com.arch.temp.tools.FileTemplateExt.getRootPathTemplate
import com.arch.temp.tools.getBasePathTemplate
import com.intellij.openapi.actionSystem.*
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.vfs.readText
import com.intellij.openapi.vfs.writeText
import com.intellij.psi.impl.PsiManagerImpl
import org.jetbrains.annotations.NonNls
import java.io.File

class MigrateTemplateAction : AnAction() {

    override fun actionPerformed(actionEvent: AnActionEvent) {

        val pathTemplateSelect = actionEvent.getData(CommonDataKeys.VIRTUAL_FILE)
        val migratePathTemplate = if (pathTemplateSelect?.parent?.path == getRootPathTemplate()) {
            actionEvent.getBasePathTemplate()
        } else {
            getRootPathTemplate()
        }
        val oldName = pathTemplateSelect?.name ?: ""
        val nameTemplate = Messages.showInputDialog(
            "Do you want to rename?",
            "Rename",
            null,
            oldName,
            null
        ) ?: return
        val idEvent = ActionManager.getInstance().getId(this) ?: ""

        pathTemplateSelect?.let { vFileSelect ->
            migrateTemplate(
                actionEvent.project!!,
                vFileSelect,
                nameTemplate,
                oldName,
                migratePathTemplate,
                idEvent
            )
        }
    }

    private fun migrateTemplate(
        project: Project,
        pathTemplateSelect: VirtualFile,
        renamePath: String,
        oldName: String,
        migratePathTemplate: String,
        idEvent: String
    ) {
        try {
            ApplicationManager.getApplication().runWriteAction {
                val mainFile = pathTemplateSelect.children
                    ?.firstOrNull {
                        it.name == MAIN_FILE_TEMPLATE
                                || it.name == MAIN_SHORT_FILE_TEMPLATE
                    }?.let {
                        val text = PsiManagerImpl.getInstance(project).findFile(it)?.text ?: it.readText()
                        JsonModelMapper.mapToMainClass(text)
                    }

                val vFIlePathTemplate = if (idEvent == Constants.ACTION_MIGRATE) {
                    pathTemplateSelect.move(null, VfsUtil.createDirectories(migratePathTemplate))
                    pathTemplateSelect.rename(null, renamePath)
                    pathTemplateSelect
                } else {
                    val vFile = VfsUtil.copy(
                        null,
                        pathTemplateSelect,
                        VfsUtil.createDirectories(migratePathTemplate)
                    )
                    vFile.rename(null, renamePath)
                    vFile
                }
                if (oldName != renamePath) {
                    mainFile?.let { mainFileJson ->
                        renameMainFile(
                            mainFileJson,
                            vFIlePathTemplate,
                            renamePath
                        )
                    }
                }
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

    private fun renameMainFile(
        mainJson: MainClassJson,
        fileIde: VirtualFile,
        nameTemplate: String
    ) {
        fileIde.children
            ?.firstOrNull {
                it.name == MAIN_FILE_TEMPLATE
                        || it.name == MAIN_SHORT_FILE_TEMPLATE
            }?.let {
                val jsonParse = JsonModelMapper.mapToString(mainJson.copy(name = nameTemplate))
                it.writeText(jsonParse)
            }
    }


    override fun getActionUpdateThread(): ActionUpdateThread {
        return ActionUpdateThread.BGT
    }

    override fun update(e: AnActionEvent) {
        val project = e.project
        val pathTemplate = e.getData(CommonDataKeys.VIRTUAL_FILE)
        val shortMainTemplate = pathTemplate?.findChild(MAIN_SHORT_FILE_TEMPLATE)
        val shortTemplate = pathTemplate?.findChild(MAIN_FILE_TEMPLATE)
        e.presentation.isEnabledAndVisible = project != null && (shortMainTemplate != null || shortTemplate != null)
    }

}