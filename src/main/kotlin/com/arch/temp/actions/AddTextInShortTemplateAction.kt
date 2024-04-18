package com.arch.temp.actions

import com.arch.temp.constant.Constants
import com.arch.temp.mapper.JsonModelMapper
import com.arch.temp.model.FileShortTemplateModel
import com.arch.temp.model.MainShortClassJson
import com.arch.temp.tools.getListShortTemplate
import com.arch.temp.tools.toTmFile
import com.arch.temp.view.CheckShortTemplateDialog
import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.MessageType
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.ui.popup.Balloon
import com.intellij.openapi.ui.popup.JBPopupFactory
import com.intellij.openapi.util.TextRange
import com.intellij.openapi.vfs.*
import com.intellij.openapi.wm.WindowManager
import com.intellij.psi.impl.PsiManagerImpl
import com.intellij.ui.awt.RelativePoint
import java.io.File


class AddTextInShortTemplateAction : AnAction() {
    override fun actionPerformed(event: AnActionEvent) {
        val editor = event.getData(CommonDataKeys.EDITOR)
        val templateList = event.getListShortTemplate()

        if (event.project == null) return
        if (templateList.isEmpty()) {
            val statusBar = WindowManager.getInstance().getStatusBar(event.project!!)
            JBPopupFactory.getInstance()
                .createHtmlTextBalloonBuilder(
                    "You not have short template",
                    MessageType.INFO,
                    null
                )
                .setFadeoutTime(7500)
                .createBalloon()
                .show(
                    RelativePoint.getCenterOf(statusBar.component!!),
                    Balloon.Position.atRight
                )
            return
        }

        if (templateList.size > 1) {
            CheckShortTemplateDialog(templateList, event.project) {
                addTemplate(event.project!!, it, editor)
            }.showAndGet()
        } else {
            addTemplate(event.project!!, templateList.first(), editor)
        }
    }

    private fun addTemplate(
        project: Project,
        result: MainShortClassJson,
        editor: Editor?
    ) {
        val nameTemplate = Messages.showInputDialog(
            "Name short Template",
            "Add Short Template",
            null,
            "",
            null
        ) ?: return


        ApplicationManager.getApplication().runWriteAction {
            LocalFileSystem.getInstance().findFileByPath(result.globalBasePath)
                ?.createChildData(null, nameTemplate.toTmFile())
                ?.let {
                    val file = File(result.globalBasePath, nameTemplate.toTmFile())
                    editor?.let { edit ->
                        val caret = edit.caretModel.primaryCaret
                        val text = edit.document.getText(
                            TextRange.from(
                                caret.selectionStart,
                                caret.selectionEnd - caret.selectionStart
                            )
                        )
                        file.writeText(text)

                        val textCorrect = file.readLines()
                            .map {
                                val column = edit.caretModel.primaryCaret.selectionStartPosition.column
                                val char = it.toCharArray()
                                if (column % 4 == 0) {
                                    if (char.isNotEmpty() && char.first().code == 32) {
                                        it.removeRange(0, column)
                                    } else {
                                        it
                                    }
                                } else {
                                    it
                                }
                            }
                            .joinToString("") { line -> "$line\n" }.trimIndent()

                        file.writeText(textCorrect)
                    }
                }

            addInMainFile(
                project,
                result,
                nameTemplate
            )
        }
    }

    private fun addInMainFile(
        project: Project,
        result: MainShortClassJson,
        nameTemplate: String
    ) {
        val mainFileTemplate = File(result.globalBasePath, Constants.MAIN_SHORT_FILE_TEMPLATE)
        LocalFileSystem.getInstance().findFileByIoFile(mainFileTemplate)?.let { mainVFile ->
            if (mainVFile.isFile) {
                val mainText = PsiManagerImpl.getInstance(project).findFile(mainVFile)?.text
                    ?: mainVFile.readText()
                val mainJson = JsonModelMapper.mapToShortMainClass(mainText)
                val listFile = mainJson.fileTemplate.toMutableList()
                listFile.add(
                    FileShortTemplateModel(
                        name = nameTemplate,
                        filePath = nameTemplate.toTmFile()
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
        val editor = e.getData(CommonDataKeys.EDITOR)
        val caret = editor?.caretModel?.primaryCaret
        e.presentation.isEnabledAndVisible = editor != null
                && caret?.selectionStart != caret?.selectionEnd
    }
}