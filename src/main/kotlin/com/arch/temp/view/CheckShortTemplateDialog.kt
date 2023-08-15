package com.arch.temp.view

import com.arch.temp.model.MainShortClassJson
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.components.JBRadioButton
import com.intellij.ui.dsl.builder.panel
import javax.swing.JComponent

class CheckShortTemplateDialog(
    private val param: List<MainShortClassJson>,
    project: Project?,
    private val callOk: (MainShortClassJson) -> Unit
) : DialogWrapper(project) {

    private val radioButtonList = mutableListOf<JBRadioButton>()

    init {
        init()
    }

    override fun createCenterPanel(): JComponent {
        return panel {
            buttonsGroup {
                param.forEach {
                    val buttonJb = JBRadioButton(it.name)
                    radioButtonList.add(buttonJb)
                    row{
                        cell(
                            buttonJb
                        )
                    }
                }
            }
        }
    }

    override fun doOKAction() {
        super.doOKAction()
        radioButtonList.forEachIndexed { index, button ->
            if (button.isSelected) {
                callOk(param[index])
            }
        }
    }

}