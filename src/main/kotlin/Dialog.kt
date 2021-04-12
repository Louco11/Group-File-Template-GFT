import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.project.impl.ProjectExImpl
import com.intellij.openapi.ui.Messages
import model.MainClassJson
import tools.CreateTemplate

class Dialog(val mainClass: MainClassJson) : AnAction(mainClass.name, mainClass.description, null) {

    override fun actionPerformed(event: AnActionEvent) {
        val currentProject = event.project
        val dlgMsg = StringBuilder(event.presentation.text + " Selected!")
        val dlgTitle = event.presentation.description

        val nav = event.getData(CommonDataKeys.NAVIGATABLE)
        if (nav != null) {
            dlgMsg.append(String.format("\nSelected Element: %s", nav.toString()))
        }
        val mapParam = mutableMapOf<String, String>()
        mainClass.param.forEach {
            mapParam[it] = Messages.showInputDialog(it, "", null).orEmpty()
        }
        mainClass.fileTemplate.forEach { file ->
            CreateTemplate.createFileTemplate(event.getData(CommonDataKeys.VIRTUAL_FILE)?.path.orEmpty(), mainClass.path, mapParam, file)
        }
//        Messages.InputDialog(currentProject, dlgMsg.toString(), dlgTitle, null)
//        Messages.showMessageDialog(currentProject, dlgMsg.toString(), dlgTitle, null)
    }

    override fun update(e: AnActionEvent) {
        // Set the availability based on whether a project is open
        val project = e.project

        e.presentation.isEnabledAndVisible = project != null
    }
}