package com.arch.temp.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.vfs.VirtualFileManager
import com.arch.temp.constant.Constants
import com.arch.temp.mapper.JsonModelMapper
import com.arch.temp.model.MainShortClassJson
import com.arch.temp.tools.getBasePathTemplate
import java.io.File

class CreateEmptyShortTemplateAction : AnAction() {

    override fun actionPerformed(event: AnActionEvent) {
        val project = event.getData(CommonDataKeys.PROJECT)
        val pathTemplate = File(project?.getBasePathTemplate().orEmpty())
        var nameTemplate = Messages.showInputDialog("", "New Template", null) ?: return
        if (nameTemplate.isEmpty()) {
            nameTemplate = Constants.EMPTY_SHORT_TEMPLATE_PATH_NAME
        }
        if (!pathTemplate.isDirectory) pathTemplate.mkdir()
        val pathNewTemplate = "${pathTemplate.path}/$nameTemplate"
        createPath(
            if (File(pathNewTemplate).isDirectory) "$pathNewTemplate${pathTemplate.list()?.size}"
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
        val mainFile = File(path, Constants.MAIN_SHORT_FILE_TEMPLATE)
        mainFile.createNewFile()
        val template = MainShortClassJson(name = path.split("/").last())
        mainFile.writeText(JsonModelMapper.mapToString(template))
    }

}