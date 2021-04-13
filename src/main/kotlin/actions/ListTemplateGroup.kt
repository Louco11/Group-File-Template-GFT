package actions

import com.intellij.openapi.actionSystem.ActionGroup
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.project.impl.ProjectExImpl
import constant.Constants
import mapper.JsonModelMapper
import tools.getListTemplate
import java.io.File
import java.nio.charset.Charset

class ListTemplateGroup : ActionGroup() {

    override fun getChildren(event: AnActionEvent?): Array<AnAction> {
        return event?.getListTemplate()?.map { DialogCreateArchitectureAction(it) }?.toTypedArray() ?: emptyArray()
    }

    override fun update(e: AnActionEvent) {
        val project = e.project
        val file = e.getData(CommonDataKeys.PSI_FILE)
        e.presentation.isEnabledAndVisible = project != null && file == null
    }
}