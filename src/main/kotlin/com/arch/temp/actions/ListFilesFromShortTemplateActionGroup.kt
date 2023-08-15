package com.arch.temp.actions

import com.arch.temp.model.MainShortClassJson
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.DefaultActionGroup

class ListFilesFromShortTemplateActionGroup(
    val mainShortClassJson: MainShortClassJson
) : DefaultActionGroup(
    mainShortClassJson.name,
    true
) {
    override fun getChildren(e: AnActionEvent?): Array<AnAction> {
        return mainShortClassJson.fileTemplate.map {
            CreateShortCodeFromTemplateAction(
                mainShortClassJson,
                it
            )
        }.toTypedArray()
    }
}