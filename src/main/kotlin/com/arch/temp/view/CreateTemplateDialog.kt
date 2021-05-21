package com.arch.temp.view

import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.layout.GrowPolicy
import com.intellij.ui.layout.panel
import javax.swing.*

class CreateTemplateDialog(
    private val param: List<String>,
    project: Project?,
    private val callOk: (Map<String, String>) -> Unit
) : DialogWrapper(project) {

    private val mapInput: MutableMap<String, JTextField> = mutableMapOf()

    init {
        init()
        title = "Fill param"
    }

    override fun createCenterPanel(): JComponent {
        return panel {
            param.forEach { param  ->
                row(param) {
                    val input = JTextField()
                    mapInput[param] = input
                    input().growPolicy(GrowPolicy.SHORT_TEXT).onApply {  }
                }
            }
        }
    }

    override fun doOKAction() {
        super.doOKAction()
        val mutableMap = mutableMapOf<String, String>()
        param.forEach {
            mutableMap[it] = mapInput[it]?.text.orEmpty()
        }
        callOk(mutableMap)
    }
}