package actions

import com.intellij.history.core.ChangeList
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.actionSystem.Constraints
import com.intellij.openapi.module.Module
import com.intellij.openapi.module.impl.ModuleImpl
import com.intellij.openapi.roots.ui.configuration.ChooseModulesDialog
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.vfs.VirtualFileManager
import constant.Constants
import mapper.JsonModelMapper
import model.FileTemplate
import org.jetbrains.annotations.NotNull
import org.jetbrains.annotations.Nullable
import tools.getListTemplate
import tools.toDrFile
import java.awt.Button
import java.io.File
import java.nio.charset.Charset

const val ADD_FILE_IN_TEMPLATE = "Add file in Template"

class AddFileInTemplate : AnAction() {

    override fun actionPerformed(event: AnActionEvent) {
        val fileToTemplate = event.getData(CommonDataKeys.VIRTUAL_FILE)
        val templateList = event.getListTemplate()

        val dialog = ChooseModulesDialog(
            Button(),
            templateList.map {
                ModuleImpl(
                    it.name,
                    event.project!!,
                    it.path
                )
            },
            ADD_FILE_IN_TEMPLATE
        )
        dialog.setSingleSelectionMode()
        val result = dialog.showAndGetResult()
        addFile(result, fileToTemplate)
        VirtualFileManager.getInstance().asyncRefresh {
            VirtualFileManager.getInstance().syncRefresh()
        }
    }

    private fun addFile(
        result: @NotNull List<Module>,
        fileToTemplate: @Nullable VirtualFile?
    ) {
        if (result.isNotEmpty()) {
            val filePathTemplate = File(result[0].moduleFile?.path.orEmpty())
            if (filePathTemplate.isDirectory) {
                val fileToTemp = File(fileToTemplate?.path.orEmpty())
                if (fileToTemp.isFile) {
                    val string = fileToTemp.readText(Charset.defaultCharset())
                    val fileName = fileToTemplate?.name.orEmpty().split(".").first()
                    val renameFileName =
                        Messages.showInputDialog("Name File Template", ADD_FILE_IN_TEMPLATE, null, fileName, null).orEmpty()
                    val file = File(filePathTemplate.path, renameFileName.toDrFile())
                    file.createNewFile()
                    file.writeText(string)
                    addFileMainFile(
                        renameFileName.toDrFile(),
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
            val mainJson = JsonModelMapper.mapToMainClassXml(mainFileTemplate.readText(Charset.defaultCharset()))
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
