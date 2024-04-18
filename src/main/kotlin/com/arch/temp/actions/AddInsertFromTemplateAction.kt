package com.arch.temp.actions

import com.arch.temp.constant.Constants
import com.arch.temp.mapper.JsonModelMapper
import com.arch.temp.model.InsertInFileTemplateModel
import com.arch.temp.model.MainClassJson
import com.arch.temp.tools.getListTemplate
import com.arch.temp.view.CheckTemplateDialog
import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.*
import com.intellij.psi.impl.PsiManagerImpl
import java.io.File


class AddInsertFromTemplateAction : AnAction() {

    override fun actionPerformed(event: AnActionEvent) {
        val editor = event.getData(CommonDataKeys.EDITOR) ?: return
        val vFile = event.getData(CommonDataKeys.VIRTUAL_FILE) ?: return
        val project = event.project ?: return
        val basePath = project.basePath ?: return

        val templateList = event.getListTemplate()

        val insertModel = InsertInFileTemplateModel(
            line = editor.caretModel.primaryCaret.selectionStartPosition.line + 1,
            path = vFile.path.split(basePath).last()
        )
        if (templateList.size > 1) {
            CheckTemplateDialog(templateList, project) {
                addInMainFile(project, it, insertModel)
            }.showAndGet()
        } else {
            addInMainFile(project, templateList.first(), insertModel)
        }

    }

    private fun addInMainFile(
        project: Project,
        result: MainClassJson,
        insertModel: InsertInFileTemplateModel
    ) {
        ApplicationManager.getApplication().runWriteAction {
            val mainFileTemplate = File(result.globalBasePath, Constants.MAIN_FILE_TEMPLATE)
            LocalFileSystem.getInstance().findFileByIoFile(mainFileTemplate)?.let { mainVFile ->
                if (mainVFile.isFile) {
                    val mainText = PsiManagerImpl.getInstance(project).findFile(mainVFile)?.text
                        ?: mainVFile.readText()
                    val mainJson = JsonModelMapper.mapToMainClass(mainText)
                    val listInsertModels = mainJson.insertInFileTemplate.toMutableList()
                    listInsertModels.add(insertModel)

                    val jsonParse = JsonModelMapper.mapToString(mainJson.copy(insertInFileTemplate = listInsertModels))
                    mainVFile.writeText(jsonParse)
                }
            }
        }
    }

    override fun getActionUpdateThread(): ActionUpdateThread {
        return ActionUpdateThread.BGT
    }

    override fun update(e: AnActionEvent) {
        val editor = e.getData(CommonDataKeys.EDITOR)
        val caret = editor?.caretModel?.primaryCaret
        e.presentation.isEnabledAndVisible = editor != null
                && caret?.selectionStart == caret?.selectionEnd
    }
}