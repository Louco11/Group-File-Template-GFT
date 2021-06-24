package com.arch.temp.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.vfs.VirtualFileManager
import com.intellij.psi.impl.PsiParserFacadeImpl
import com.arch.temp.constant.Constants.TagXml.DEFAULT_TAG_PACKAGE
import com.arch.temp.model.MainClassJson
import com.arch.temp.tools.CreateTemplate
import com.arch.temp.tools.getPackage
import com.arch.temp.view.CreateTemplateDialog

class DialogCreateArchitectureAction(val mainClass: MainClassJson) : AnAction(mainClass.name, mainClass.description, null) {

    override fun actionPerformed(event: AnActionEvent) {
        if (mainClass.param.isEmpty()) {
            createTemplate(mapOf(), event)
        } else {
            CreateTemplateDialog(mainClass.param, event.project) { inputMap ->
                createTemplate(inputMap, event)
            }.showAndGet()
        }
    }

    private fun createTemplate(
        inputMap: Map<String, String>,
        event: AnActionEvent
    ) {
        val mapParam = inputMap.toMutableMap()
        PsiParserFacadeImpl(event.project!!)
        mapParam[DEFAULT_TAG_PACKAGE] = event.getPackage()
        val basePath = event.getData(CommonDataKeys.PROJECT)?.basePath.orEmpty()
        val pathCreate = "$basePath${mainClass.path}"
        mainClass.fileTemplate.forEach { file ->
            CreateTemplate.createFileTemplate(
                event.getData(CommonDataKeys.VIRTUAL_FILE)?.path.orEmpty(),
                pathCreate,
                mapParam,
                file
            )
        }
        VirtualFileManager.getInstance().asyncRefresh {
            VirtualFileManager.getInstance().syncRefresh()
        }
    }

}