package com.arch.temp.tools

import com.arch.temp.constant.Constants
import com.arch.temp.model.FileTemplate
import com.intellij.codeInsight.template.TemplateManager
import com.intellij.ide.fileTemplates.FileTemplateManager
import com.intellij.ide.scratch.RootType
import com.intellij.ide.scratch.ScratchFileServiceImpl
import com.intellij.openapi.application.PathManager
import com.intellij.openapi.util.io.FileUtilRt

fun FileTemplate.getPath() =
    if (path.isNotEmpty() && path.toCharArray().first() == '/') path.removeRange(0, 1) else path

object FileTemplateExt {
    private var rootPath: String = RootType.getAllRootTypes().find {
        it.id == Constants.ExtensionConst.ROOT_ID
    }?.let { rootType ->
        ScratchFileServiceImpl.getInstance().getRootPath(rootType)
    } ?: PathManager.getPluginTempPath()

    fun getRootPathTemplate(): String {
        return FileUtilRt.toSystemDependentName(rootPath)
    }
}