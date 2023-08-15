package com.arch.temp.actions

import com.intellij.openapi.actionSystem.AnActionEvent

import com.intellij.openapi.actionSystem.DefaultActionGroup


class CustomDefaultActionGroup : DefaultActionGroup() {
    override fun update(event: AnActionEvent) {
        // Enable/disable depending on whether a user is editing...
    }
}