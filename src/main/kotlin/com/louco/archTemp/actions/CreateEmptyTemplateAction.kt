package com.louco.archTemp.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.vfs.VirtualFileManager
import com.louco.archTemp.constant.Constants
import com.louco.archTemp.mapper.JsonModelMapper
import com.louco.archTemp.model.MainClassJson
import java.io.File

class CreateEmptyTemplateAction : AnAction() {

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
            else pathNewTemplate
        )
        VirtualFileManager.getInstance().asyncRefresh {
            VirtualFileManager.getInstance().syncRefresh()
        }
    }

    private fun createPath(path: String) {
        File(path).mkdir()
        createMainFileTemplate(path)
    }

    private fun createMainFileTemplate(path: String) {
        val mainFile = File(path, Constants.MAIN_FILE_TEMPLATE)
        mainFile.createNewFile()
        val template = MainClassJson(name = path.split("/").last(), path = path)
        mainFile.writeText(JsonModelMapper.mapToString(template))
    }

}