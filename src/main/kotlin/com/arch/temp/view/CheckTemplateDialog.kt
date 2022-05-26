package com.arch.temp.view

import com.arch.temp.model.MainClassJson
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.dsl.builder.ButtonsGroup
import com.intellij.ui.dsl.builder.MutableProperty
import com.intellij.ui.dsl.builder.panel
import javax.swing.JComponent

class CheckTemplateDialog(
    private val param: List<MainClassJson>,
    project: Project?,
    private val callOk: (MainClassJson) -> Unit
) : DialogWrapper(project) {

    private var valueCheck: MainClassJson = param.first()

    init { super.init() }

    override fun createCenterPanel(): JComponent {
        return panel {
            buttonsGroup{
                param.forEach { value ->
                    row { radioButton(value.name, value) }
                }
            }.binding({ valueCheck }, { valueCheck = it })
        }
    }

    private inline fun <reified T : Any> ButtonsGroup.binding(
        noinline getter: () -> T,
        noinline setter: (T) -> Unit
    ): ButtonsGroup {
        return bind(MutableProperty(getter, setter), T::class.java)
    }

    override fun doOKAction() {
        super.doOKAction()
        callOk(valueCheck)
    }
}
