package com.arch.temp.actions

import com.intellij.openapi.actionSystem.ActionGroup
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.arch.temp.tools.getListTemplate

class ListTemplateActionGroup : ActionGroup() {

    override fun getChildren(event: AnActionEvent?): Array<AnAction> {
        return event?.getListTemplate()?.map { CreateStructureFromTemplateAction(it) }?.toTypedArray() ?: emptyArray()
    }

    override fun update(e: AnActionEvent) {
        val project = e.project
        val file = e.getData(CommonDataKeys.PSI_FILE)
        e.presentation.isEnabledAndVisible = project != null && file == null
    }
}