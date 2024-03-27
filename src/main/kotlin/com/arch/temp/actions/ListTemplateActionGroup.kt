package com.arch.temp.actions

import com.arch.temp.tools.getListTemplate
import com.intellij.openapi.actionSystem.*

class ListTemplateActionGroup : ActionGroup() {

    override fun getChildren(event: AnActionEvent?): Array<AnAction> {
        return event?.getListTemplate()?.map { CreateStructureFromTemplateAction(it) }?.toTypedArray() ?: emptyArray()
    }

    override fun getActionUpdateThread(): ActionUpdateThread {
        return ActionUpdateThread.BGT
    }

    override fun update(e: AnActionEvent) {
        val project = e.project
        val file = e.getData(CommonDataKeys.PSI_FILE)
        e.presentation.isEnabledAndVisible = project != null && file == null
    }
}