package com.arch.temp.view

import com.arch.temp.model.MainClassJson
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.dsl.builder.Align
import com.intellij.ui.dsl.builder.AlignX
import com.intellij.ui.dsl.builder.RowLayout
import com.intellij.ui.dsl.builder.panel
import java.awt.Dimension
import javax.swing.JComboBox
import javax.swing.JComponent
import javax.swing.JTextArea
import javax.swing.JTextField


const val TITLE_DIALOG = "Fill param"

class CreateTemplateDialog(
    project: Project?,
    private val mainClassJson: MainClassJson,
    private val callOk: (Map<String, String>) -> Unit
) : DialogWrapper(project) {

    private val mapInput: MutableMap<String, JTextField> = mutableMapOf()
    private val mapComboBox: MutableMap<String, JComboBox<String>> = mutableMapOf()

    init {
        init()
        title = TITLE_DIALOG
    }

    override fun createCenterPanel(): JComponent {
        return panel {
            if (mainClassJson.description.isNotEmpty()) {
                row {
                    val textArea = JTextArea().apply {
                        lineWrap = true
                        wrapStyleWord = true
                        text = mainClassJson.description
                    }
                    cell(textArea).resizableColumn().align(AlignX.FILL)
                }
            }
            mainClassJson.param.forEach { param ->
                row(param) {
                    val input = JTextField(20)
                    mapInput[param] = input
                    cell(input)
                }
            }
            mainClassJson.selectParam.forEach {
                row(it.paramName) {
                    val combo = JComboBox(it.paramValue.toTypedArray())
                    combo.preferredSize = Dimension(223, 30)
                    mapComboBox[it.paramName] = combo
                    cell(combo)
                }
            }
        }
    }

    override fun doOKAction() {
        super.doOKAction()
        val mutableMap = mutableMapOf<String, String>()
        mainClassJson.param.forEach {
            mutableMap[it] = mapInput[it]?.text.orEmpty()
        }
        mainClassJson.selectParam.forEach {
            mutableMap[it.paramName] = (mapComboBox[it.paramName]?.selectedItem as String)
        }
        callOk(mutableMap)
    }
}