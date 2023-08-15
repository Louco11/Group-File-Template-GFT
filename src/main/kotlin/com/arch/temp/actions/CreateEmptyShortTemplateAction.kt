package com.arch.temp.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.vfs.VirtualFileManager
import com.arch.temp.constant.Constants
import com.arch.temp.mapper.JsonModelMapper
import com.arch.temp.model.MainShortClassJson
import java.io.File

class CreateEmptyShortTemplateAction : AnAction() {

    override fun actionPerformed(event: AnActionEvent) {
        val basePath = event.getData(CommonDataKeys.PROJECT)?.basePath.orEmpty()
        val pathTemplate = File("$basePath${Constants.PATH_TEMPLATE}")
        var nameTemplate = Messages.showInputDialog("", "New Template", null) ?: return
        if (nameTemplate.isEmpty()) {
            nameTemplate = Constants.EMPTY_TEMPLATE_PATH_NAME
        }
        if (!pathTemplate.isDirectory) pathTemplate.mkdir()
        val pathNewTemplate = "${pathTemplate.path}/$nameTemplate"
        createPath(
            if (File(pathNewTemplate).isDirectory) "$pathNewTemplate${pathTemplate.list().size}"
            else pathNewTemplate,
            "${Constants.PATH_TEMPLATE}/$nameTemplate"
        )
        VirtualFileManager.getInstance().asyncRefresh {
            VirtualFileManager.getInstance().syncRefresh()
        }
    }

    private fun createPath(path: String, pathInFile: String) {
        File(path).mkdir()
        createMainFileTemplate(path, pathInFile)
    }

    private fun createMainFileTemplate(path: String, pathInFile: String) {
        val mainFile = File(path, Constants.MAIN_SHORT_FILE_TEMPLATE)
        mainFile.createNewFile()
        val template = MainShortClassJson(name = path.split("/").last(), path = pathInFile)
        mainFile.writeText(JsonModelMapper.mapToString(template))
    }

}