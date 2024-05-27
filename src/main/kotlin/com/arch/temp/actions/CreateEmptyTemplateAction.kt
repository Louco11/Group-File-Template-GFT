package com.arch.temp.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.vfs.VirtualFileManager
import com.arch.temp.constant.Constants
import com.arch.temp.constant.Constants.SPLASH
import com.arch.temp.mapper.JsonModelMapper
import com.arch.temp.model.MainClassJson
import com.arch.temp.model.MainShortClassJson
import com.arch.temp.tools.getBasePathTemplate
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.openapi.vfs.VirtualFile
import java.io.File

class CreateEmptyTemplateAction : AnAction() {

    override fun actionPerformed(event: AnActionEvent) {
        val project = event.getData(CommonDataKeys.PROJECT)
        val pathTemplate = File(project?.getBasePathTemplate().orEmpty())

        var nameTemplate = Messages.showInputDialog("", "New Template", null) ?: return
        if (nameTemplate.isEmpty()) {
            nameTemplate = Constants.EMPTY_TEMPLATE_PATH_NAME
        }

        if (!pathTemplate.isDirectory) pathTemplate.mkdir()
        val pathNewTemplate = "${pathTemplate.path}$SPLASH$nameTemplate"

        createPath(
            if (File(pathNewTemplate).isDirectory) "$pathNewTemplate${pathTemplate.list()?.size}"
            else pathNewTemplate
        )

    }

    private fun createPath(path: String) {
        val vFile = VfsUtil.createDirectories(path)
        createMainFileTemplate(vFile)
    }

    private fun createMainFileTemplate(vFile: VirtualFile) {
        ApplicationManager.getApplication().runWriteAction {
            val mainFile = vFile.createChildData(null, Constants.MAIN_FILE_TEMPLATE)
            val template = MainShortClassJson(name = vFile.path.split(SPLASH).last())
            mainFile.setBinaryContent(JsonModelMapper.mapToString(template).toByteArray())
        }
    }

}