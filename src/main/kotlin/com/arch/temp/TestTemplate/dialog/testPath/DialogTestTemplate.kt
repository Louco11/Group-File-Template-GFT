package com.arch.temp.TestTemplate.dialog.testPath

import com.arch.temp.model.MainClassJson
import javax.swing.JRadioButton

class DialogTestTemplate(
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