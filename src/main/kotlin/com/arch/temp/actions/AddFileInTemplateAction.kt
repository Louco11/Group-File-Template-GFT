package com.arch.temp.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.ui.Messages
import com.arch.temp.constant.Constants
import com.arch.temp.mapper.JsonModelMapper
import com.arch.temp.model.FileTemplateModel
import com.arch.temp.model.MainClassJson
import com.arch.temp.tools.getListTemplate
import com.arch.temp.tools.toTmFile
import com.arch.temp.view.CheckTemplateDialog
import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.*
import com.intellij.psi.impl.PsiManagerImpl
import java.io.File

const val ADD_FILE_IN_TEMPLATE = "Add file in Template"
const val NAME_FILE_IN_TEMPLATE = "Name File Template"

class AddFileInTemplate : AnAction() {

    override fun actionPerformed(event: AnActionEvent) {
        val fileToTemplate = event.getData(CommonDataKeys.VIRTUAL_FILE) ?: return
        val project = event.project ?: return

        val templateList = event.getListTemplate()

        if (templateList.size > 1) {
            CheckTemplateDialog(templateList, project) {
                addFile(project, it, fileToTemplate)
            }.showAndGet()
        } else {
            addFile(project, templateList.first(), fileToTemplate)
        }

    }

    private fun addFile(
        project: Project,
        result: MainClassJson,
        fileToTemplate: VirtualFile
    ) {
        if (fileToTemplate.isFile) {
            val contentFile = fileToTemplate.readText()
            val fileName = fileToTemplate.name.split(".").first()

            val renameFileName = Messages.showInputDialog(
                NAME_FILE_IN_TEMPLATE,
                ADD_FILE_IN_TEMPLATE,
                null,
                fileName,
                null
            ) ?: return

            ApplicationManager.getApplication().runWriteAction {
                LocalFileSystem.getInstance().findFileByPath(result.globalBasePath)
                    ?.createChildData(
                        null,
                        renameFileName.toTmFile()
                    )?.writeText(contentFile)

                addFileMainFile(
                    project,
                    renameFileName.toTmFile(),
                    result,
                    fileToTemplate
                )
            }
        }
    }

    private fun addFileMainFile(
        project: Project,
        fileName: String,
        result: MainClassJson,
        fileToTemplate: VirtualFile
    ) {
        val mainFile = File(result.globalBasePath, Constants.MAIN_FILE_TEMPLATE)
        LocalFileSystem.getInstance().findFileByIoFile(mainFile)?.let { mainVFile ->
            if (mainVFile.isFile) {
                val mainText = PsiManagerImpl.getInstance(project).findFile(mainVFile)?.text
                    ?: mainVFile.readText()
                val mainJson = JsonModelMapper.mapToMainClass(mainText)
                val listFile = mainJson.fileTemplate.toMutableList()
                listFile.add(
                    FileTemplateModel(
                        name = fileToTemplate.name,
                        fileTemplatePath = fileName
                    )
                )
                val jsonParse = JsonModelMapper.mapToString(mainJson.copy(fileTemplate = listFile))
                mainVFile.writeText(jsonParse)
            }
        }


    }

    override fun getActionUpdateThread(): ActionUpdateThread {
        return ActionUpdateThread.BGT
    }

    override fun update(e: AnActionEvent) {
        val project = e.project
        val file = e.getData(CommonDataKeys.PSI_FILE)
        e.presentation.isEnabledAndVisible = project != null && file != null
    }
}