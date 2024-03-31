package com.arch.temp.actions

import com.arch.temp.constant.Constants
import com.arch.temp.mapper.JsonModelMapper
import com.arch.temp.model.FileShortTemplate
import com.arch.temp.model.MainShortClassJson
import com.arch.temp.tools.getListShortTemplate
import com.arch.temp.tools.toTmFile
import com.arch.temp.view.CheckShortTemplateDialog
import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.util.TextRange
import java.io.File
import java.nio.charset.Charset

class AddTextInShortTemplateAction : AnAction() {
    override fun actionPerformed(event: AnActionEvent) {
        val editor = event.getData(CommonDataKeys.EDITOR)
        val templateList = event.getListShortTemplate()

        if (templateList.size > 1) {
            CheckShortTemplateDialog(templateList, event.project) {
                addTemplate(it, editor)
            }.showAndGet()
        } else {
            addTemplate(templateList.first(), editor)
        }
    }

    private fun addTemplate(result: MainShortClassJson, editor: Editor?) {
        val nameTemplate = Messages.showInputDialog(
                "Name short Template",
                "Add Short Template",
                null,
                "",
                null
            ) ?: return

        val file = File(result.globalBasePath, nameTemplate.toTmFile())
        file.createNewFile()
        editor?.let{edit ->
            val caret = edit.caretModel.primaryCaret
            val text = edit.document.getText(
                TextRange.from(
                    caret.selectionStart,
                    caret.selectionEnd-caret.selectionStart
                )
            )
            file.writeText(text)

            val textCorrect = file.readLines()
                .map{
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

        addInMainFile(
            result,
            nameTemplate
        )
    }

    private fun addInMainFile(result: MainShortClassJson, nameTemplate: String) {
        val mainFileTemplate = File(result.globalBasePath, Constants.MAIN_SHORT_FILE_TEMPLATE)
        if (mainFileTemplate.isFile) {
            val mainJson = JsonModelMapper.mapToShortMainClass(mainFileTemplate.readText(Charset.defaultCharset()))
            val listFile = mainJson.fileTemplate.toMutableList()
            listFile.add(
                FileShortTemplate(
                    name = nameTemplate,
                    filePath = nameTemplate.toTmFile()
                )
            )
            val jsonParse = JsonModelMapper.mapToString(mainJson.copy(fileTemplate = listFile))
            mainFileTemplate.writeText(jsonParse)
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