package com.arch.temp.view

import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.components.JBTextField
import com.intellij.ui.dsl.builder.Cell
import com.intellij.ui.dsl.builder.panel
import javax.swing.*

const val TITLE_DIALOG = "Fill param"

class CreateTemplateDialog(
    private val param: List<String>,
    project: Project?,
    private val callOk: (Map<String, String>) -> Unit
) : DialogWrapper(project) {

    private val mapInput: MutableMap<String, Cell<JBTextField>> = mutableMapOf()

    init {
        init()
        title = TITLE_DIALOG
    }

    override fun createCenterPanel(): JComponent {
        return panel {
            param.forEach { param  ->
                row(param) {
                    val input = textField()
                    mapInput[param] = input
                    input.component
                }
            }
        }
    }

    override fun doOKAction() {
        super.doOKAction()
        val mutableMap = mutableMapOf<String, String>()
        param.forEach {
            mutableMap[it] = mapInput[it]?.component?.text.orEmpty()
        }
        callOk(mutableMap)
    }
}