package com.arch.temp.view

import com.arch.temp.model.MainClassJson
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.layout.panel
import javax.swing.JComponent

class DialogCheckTemplate(
    private val param: List<MainClassJson>,
    project: Project?,
    private val callOk: (MainClassJson) -> Unit
) : DialogWrapper(project) {

    init {
        init()
    }

    override fun createCenterPanel(): JComponent {
        return panel {
            buttonGroup{
                param.forEachIndexed {index, mainclass ->
                    row {
                        radioButton(mainclass.name)
                    }
                }
            }
        }
    }


    override fun doOKAction() {
        super.doOKAction()

    }

}