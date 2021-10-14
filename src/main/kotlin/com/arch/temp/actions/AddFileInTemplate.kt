package com.arch.temp.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.vfs.VirtualFileManager
import com.arch.temp.constant.Constants
import com.arch.temp.mapper.JsonModelMapper
import com.arch.temp.model.FileTemplate
import com.arch.temp.model.MainClassJson
import com.arch.temp.tools.getListTemplate
import com.arch.temp.tools.toTmFile
import com.arch.temp.view.CheckTemplateDialog
import java.io.File
import java.nio.charset.Charset

const val ADD_FILE_IN_TEMPLATE = "Add file in Template"
const val NAME_FILE_IN_TEMPLATE = "Name File Template"

class AddFileInTemplate : AnAction() {

    override fun actionPerformed(event: AnActionEvent) {
        val fileToTemplate = event.getData(CommonDataKeys.VIRTUAL_FILE)
        val templateList = event.getListTemplate()
        if (templateList.size > 1) {
            CheckTemplateDialog(templateList, event.project!!) {
                addFile(it, fileToTemplate)
            }.showAndGet()
        } else {
            addFile(templateList.first(), fileToTemplate)
        }
        VirtualFileManager.getInstance().asyncRefresh {
            VirtualFileManager.getInstance().syncRefresh()
        }
    }

    private fun addFile(
        result: MainClassJson,
        fileToTemplate: VirtualFile?
    ) {
        val filePathTemplate = File(result.path)
        if (filePathTemplate.isDirectory) {
            val fileToTemp = File(fileToTemplate?.path.orEmpty())
            if (fileToTemp.isFile) {
                val string = fileToTemp.readText(Charset.defaultCharset())
                val fileName = fileToTemplate?.name.orEmpty().split(".").first()
                val renameFileName =
                    Messages.showInputDialog(NAME_FILE_IN_TEMPLATE, ADD_FILE_IN_TEMPLATE, null, fileName, null).orEmpty()
                val file = File(filePathTemplate.path, renameFileName.toTmFile())
                file.createNewFile()
                file.writeText(string)
                addFileMainFile(
                    renameFileName.toTmFile(),
                    result,
                    fileToTemplate!!
                )
            }
        }
    }

    private fun addFileMainFile(
        fileName: String,
        result: MainClassJson,
        fileToTemplate: VirtualFile
    ) {
        val mainFileTemplate = File(result.path, Constants.MAIN_FILE_TEMPLATE)
        if (mainFileTemplate.isFile) {
            val mainJson = JsonModelMapper.mapToMainClass(mainFileTemplate.readText(Charset.defaultCharset()))
            val listFile = mainJson.fileTemplate.toMutableList()
            listFile.add(
                FileTemplate(
                    name = fileToTemplate.name,
                    fileTemplatePath = fileName
                )
            )
            val jsonParce = JsonModelMapper.mapToString(mainJson.copy(fileTemplate = listFile))
            mainFileTemplate.writeText(jsonParce)
        }
    }
    override fun update(e: AnActionEvent) {
        val project = e.project
        val file = e.getData(CommonDataKeys.PSI_FILE)
        e.presentation.isEnabledAndVisible = project != null && file != null
    }
}
