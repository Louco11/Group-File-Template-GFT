package com.arch.temp.actions

import com.arch.temp.model.FileShortTemplateModel
import com.arch.temp.model.MainShortClassJson
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.editor.ScrollType
import org.jetbrains.annotations.NonNls
import java.io.File

class CreateShortCodeFromTemplateAction(
    private val mainShortClassJson: MainShortClassJson,
    private val fileShortTemplate: FileShortTemplateModel
) : AnAction(
    fileShortTemplate.name,
    fileShortTemplate.filePath,
    null
) {

    override fun actionPerformed(event: AnActionEvent) {
        val editor = event.getData(CommonDataKeys.EDITOR)
        val project = event.project
        val document = editor?.document
        val file = File(mainShortClassJson.globalBasePath, fileShortTemplate.filePath)
        editor?.let { edit ->
            edit.caretModel.primaryCaret.let {
                val start = it.selectionStart
                val text = createText(it.visualPosition.column, file).trimEnd()
                if (it.selectionStart == it.selectionEnd) {
                    WriteCommandAction.runWriteCommandAction(project) {
                        document?.insertString(
                            start,
                            text
                        )
                    }
                } else {
                    WriteCommandAction.runWriteCommandAction(project) {
                        document?.replaceString(
                            start,
                            it.selectionEnd,
                            text
                        )
                    }
                    it.removeSelection()
                }
                it.moveToOffset(it.selectionStart + text.length)
                edit.scrollingModel.scrollTo(it.logicalPosition, ScrollType.RELATIVE)
            }
        }
    }

    private fun createText(column: Int, readText: File): @NonNls String {
        return if (column % 4 == 0) {
            val count = column / 4
            readText.readLines()
                .map {
                    val sb = StringBuilder()
                    for (i in 1..count) {
                        sb.append("\t")
                    }
                    sb.append(it)
                    sb.toString()
                }
                .joinToString("") { "$it\n" }
                .trimStart()
        } else {
            readText.readLines()
                .map { it.padStart(column) }
                .joinToString("") { "$it\n" }
                .trimMargin()
        }
    }

}