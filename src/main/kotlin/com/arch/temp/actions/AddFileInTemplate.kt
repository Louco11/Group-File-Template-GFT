package com.arch.temp.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.module.Module
import com.intellij.openapi.module.impl.ModuleImpl
import com.intellij.openapi.roots.ui.configuration.ChooseModulesDialog
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.vfs.VirtualFileManager
import com.arch.temp.constant.Constants
import com.arch.temp.mapper.JsonModelMapper
import com.arch.temp.model.FileTemplate
import com.arch.temp.tools.getListTemplate
import com.arch.temp.tools.toTmFile
import java.awt.Button
import java.io.File
import java.nio.charset.Charset

const val ADD_FILE_IN_TEMPLATE = "Add file in Template"

class AddFileInTemplate : AnAction() {

    override fun actionPerformed(event: AnActionEvent) {
        val fileToTemplate = event.getData(CommonDataKeys.VIRTUAL_FILE)
        val templateList = event.getListTemplate()
        val modules = templateList.map {
            ModuleImpl(
                it.name,
                event.project!!,
                it.path
            )
        }
        var result = modules.toList()
        if (modules.size > 1) {
            val dialog = ChooseModulesDialog(
                Button(),
                modules,
                ADD_FILE_IN_TEMPLATE
            )
            dialog.setSingleSelectionMode()
            result = dialog.showAndGetResult() as List<ModuleImpl>
        }
        addFile(result, fileToTemplate)
        VirtualFileManager.getInstance().asyncRefresh {
            VirtualFileManager.getInstance().syncRefresh()
        }
    }

    private fun addFile(
        result: List<Module>,
        fileToTemplate: VirtualFile?
    ) {
        if (result.isNotEmpty()) {
            val filePathTemplate = File(result.first().moduleFile?.path.orEmpty())
            if (filePathTemplate.isDirectory) {
                val fileToTemp = File(fileToTemplate?.path.orEmpty())
                if (fileToTemp.isFile) {
                    val string = fileToTemp.readText(Charset.defaultCharset())
                    val fileName = fileToTemplate?.name.orEmpty().split(".").first()
                    val renameFileName =
                        Messages.showInputDialog("Name File Template", ADD_FILE_IN_TEMPLATE, null, fileName, null).orEmpty()
                    val file = File(filePathTemplate.path, renameFileName.toTmFile())
                    file.createNewFile()
                    file.writeText(string)
                    addFileMainFile(
                        renameFileName.toTmFile(),
                        result[0],
                        fileToTemplate!!
                    )
                }
            }
        }
    }

    private fun addFileMainFile(
        fileName: String,
        result: Module,
        fileToTemplate: VirtualFile
    ) {
        val mainFileTemplate = File(result.moduleFile?.path.orEmpty(), Constants.MAIN_FILE_TEMPLATE)
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
