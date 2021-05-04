package com.louco.archTemp.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.vfs.VirtualFileManager
import com.intellij.psi.impl.PsiParserFacadeImpl
import com.louco.archTemp.constant.Constants.TagXml.DEFAULT_TAG_PACKAGE
import com.louco.archTemp.model.MainClassJson
import com.louco.archTemp.tools.CreateTemplate
import com.louco.archTemp.tools.getPackage

class DialogCreateArchitectureAction(val mainClass: MainClassJson) : AnAction(mainClass.name, mainClass.description, null) {

    override fun actionPerformed(event: AnActionEvent) {
        val mapParam = mutableMapOf<String, String>()

        mainClass.param.forEach {
            mapParam[it] = Messages.showInputDialog(it, "", null) ?: return
        }
        PsiParserFacadeImpl(event.project!!)
        mapParam[DEFAULT_TAG_PACKAGE] = event.getPackage()
        mainClass.fileTemplate.forEach { file ->
            CreateTemplate.createFileTemplate(event.getData(CommonDataKeys.VIRTUAL_FILE)?.path.orEmpty(), mainClass.path, mapParam, file)
        }
        VirtualFileManager.getInstance().asyncRefresh {
            VirtualFileManager.getInstance().syncRefresh()
        }

    }
    
}