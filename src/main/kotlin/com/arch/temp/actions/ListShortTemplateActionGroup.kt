package com.arch.temp.actions

import com.arch.temp.tools.getListShortTemplate
import com.intellij.openapi.actionSystem.ActionGroup
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent

class ListShortTemplateActionGroup : ActionGroup() {
    override fun getChildren(event: AnActionEvent?): Array<AnAction> {
        return event?.getListShortTemplate()?.map {
            ListFilesFromShortTemplateActionGroup(it)
        }?.toTypedArray() ?: emptyArray()
    }
}