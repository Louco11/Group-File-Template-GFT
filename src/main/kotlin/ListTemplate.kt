import com.intellij.openapi.actionSystem.ActionGroup
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.project.impl.ProjectExImpl
import constant.Constants
import java.io.File

class ListTemplate : ActionGroup() {

    override fun getChildren(event: AnActionEvent?): Array<AnAction> {
        val listTemplate = mutableListOf<AnAction>()

        event?.getData(CommonDataKeys.PROJECT)?.let {
            val basePath = "${(it as ProjectExImpl).basePath}${Constants.PATH_TEMPLATE}"
            val dirTemplate = File(basePath)
            if(dirTemplate.isDirectory) {
                dirTemplate.list().forEach { templatePath ->
                    listTemplate.add(
                        Dialog(templatePath, "")
                    )
                }
            }
        }

        return listTemplate.toTypedArray()
    }
}