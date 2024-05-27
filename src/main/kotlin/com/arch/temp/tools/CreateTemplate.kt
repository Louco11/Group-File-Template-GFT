package com.arch.temp.tools

import com.arch.temp.constant.Constants
import com.arch.temp.constant.Constants.SPLASH
import com.arch.temp.model.FileTemplateModel
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.vfs.*
import com.intellij.psi.impl.PsiManagerImpl
import java.io.File

object CreateTemplate {

    fun createFileTemplate(
        project: Project,
        selectPath: String,
        pathTemplate: String,
        mapParam: Map<String, String>,
        fileTemplateModel: FileTemplateModel
    ) {
        ApplicationManager.getApplication().runWriteAction {
            if (fileTemplateModel.name.isEmpty()) {
                val fileTemplatePath = fileTemplateModel.getPath(SPLASH)
                create(
                    selectPath,
                    fileTemplatePath.replaceTemplate(mapParam)
                )
            } else {
                createFile(
                    project,
                    pathTemplate,
                    selectPath,
                    fileTemplateModel,
                    mapParam
                )
            }
        }
    }

    private fun createFile(
        project: Project,
        pathTemplate: String,
        pathChoose: String,
        fileTemplateModel: FileTemplateModel,
        mapParam: Map<String, String>,
    ) {
        val nameFile = fileTemplateModel.name.replaceTemplate(mapParam)
        val fileTemplate = File(pathTemplate, fileTemplateModel.fileTemplatePath)
        if (fileTemplate.isFile) {
            val fileTemplatePath = fileTemplateModel.getPath(SPLASH)

            val filePath = when (fileTemplatePath.split(SPLASH).first()) {
                Constants.CreatePackage.ANDROID_RES -> createPathRes(
                    pathChoose,
                    fileTemplatePath.replaceTemplate(mapParam)
                )

                Constants.CreatePackage.PATH_TEST -> createPathTest(
                    pathChoose,
                    fileTemplatePath.replaceTemplate(mapParam)
                )

                Constants.CreatePackage.PATH_PROJECT -> createPathProject(
                    project.basePath!!,
                    fileTemplatePath.replaceTemplate(mapParam)
                )

                else -> create(pathChoose, fileTemplatePath.replaceTemplate(mapParam))
            }
            try {
                LocalFileSystem.getInstance().findFileByIoFile(fileTemplate)?.let {
                    val fileTemplateInPath = filePath.createChildData(null, nameFile)
                    val text = PsiManagerImpl.getInstance(project).findFile(it)?.text
                        ?: fileTemplate.readText()

                    fileTemplateInPath.writeText(text.replaceTemplate(mapParam))
                }
            } catch (e: Exception) {
                showError("No such file or directory (${nameFile})")
            }
        } else {
            showError("File template (${fileTemplateModel.fileTemplatePath}) not found")
        }
    }

    private fun createPathRes(pathProject: String, pathFileTemplate: String): VirtualFile {
        val mainJava = Constants.CreatePackage.MAIN_JAVA
        val mainKotlin = Constants.CreatePackage.MAIN_KOTLIN
        var packge = ""
        if (pathProject.indexOf(mainJava) > 0) {
            packge = pathProject.removeRange(pathProject.indexOf(mainJava), pathProject.length)
        }
        if (pathProject.indexOf(mainKotlin) > 0) {
            packge = pathProject.removeRange(pathProject.indexOf(mainKotlin), pathProject.length)
        }
        return create(packge, pathFileTemplate)
    }

    private fun createPathTest(pathProject: String, pathFileTemplate: String): VirtualFile {
        val pathTest = "${Constants.CreatePackage.PATH_TEST}$SPLASH"
        val path = pathProject.replace(Constants.CreatePackage.MAIN_PATH, Constants.CreatePackage.PATH_TEST)
            .split(pathTest)

        val pathTemplate = "$pathTest${path.last()}$SPLASH" +
                pathFileTemplate.replace(Constants.CreatePackage.PATH_TEST, "")

        return create(path.first(), pathTemplate)
    }

    private fun createPathProject(pathProject: String, pathFileTemplate: String): VirtualFile {
        val pathFile = if (pathFileTemplate.length >= 2) {
            pathFileTemplate.removeRange(0, 2)
        } else {
            pathFileTemplate.removeRange(0, 1)
        }
        return create(pathProject, pathFile)
    }

    private fun create(pathProject: String, pathFileTemplate: String): VirtualFile {
        val path = "$pathProject$SPLASH$pathFileTemplate"
        val formatPath = if (path[path.length-1] == SPLASH) path.substring(0, path.length-1) else path
        return try {
             if (!File(formatPath).isDirectory) {
                VfsUtil.createDirectories(formatPath)
            } else {
                LocalFileSystem.getInstance().findFileByPath(formatPath)!!
            }
        } catch (e: Exception) {
            LocalFileSystem.getInstance().findFileByPath(formatPath)!!
        }
    }

    private fun showError(message: String) {
        Messages.showDialog(
            message,
            "Error",
            listOf("OK").toTypedArray(),
            0,
            Messages.getWarningIcon()
        )
    }
}
