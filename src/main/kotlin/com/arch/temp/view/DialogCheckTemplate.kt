package com.arch.temp.view

import com.arch.temp.model.MainClassJson
import com.intellij.credentialStore.ProviderType
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.components.JBRadioButton
import com.intellij.ui.layout.GrowPolicy
import com.intellij.ui.layout.panel
import javax.swing.JComponent
import javax.swing.JRadioButton

class DialogCheckTemplate(
    private val param: List<MainClassJson>,
    project: Project?,
    private val callOk: (MainClassJson) -> Unit
) : DialogWrapper(project) {

    private val radioButtonList = mutableListOf<JBRadioButton>()

    init {
        init()
    }

    override fun createCenterPanel(): JComponent {
        return panel {
            buttonGroup{
                param.forEach {
                    val buttonJb = JBRadioButton(it.name)
                    radioButtonList.add(buttonJb)
                    row { buttonJb()}
                }
            }
        }
    }

    override fun doOKAction() {
        super.doOKAction()
        radioButtonList.forEachIndexed{index, button ->
            if (button.isSelected) {
                callOk(param[index])
            }
        }
    }

}