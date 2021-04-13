package actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.vfs.VirtualFileManager
import model.MainClassJson
import tools.CreateTemplate

class DialogCreateArchitectureAction(val mainClass: MainClassJson) : AnAction(mainClass.name, mainClass.description, null) {

    override fun actionPerformed(event: AnActionEvent) {
        val mapParam = mutableMapOf<String, String>()
        mainClass.param.forEach {
            mapParam[it] = Messages.showInputDialog(it, "", null).orEmpty()
        }
        mainClass.fileTemplate.forEach { file ->
            CreateTemplate.createFileTemplate(event.getData(CommonDataKeys.VIRTUAL_FILE)?.path.orEmpty(), mainClass.path, mapParam, file)
        }
        VirtualFileManager.getInstance().asyncRefresh {
            VirtualFileManager.getInstance().syncRefresh()
        }

    }
    
}