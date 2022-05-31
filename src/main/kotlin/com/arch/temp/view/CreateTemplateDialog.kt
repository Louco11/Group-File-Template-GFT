package com.arch.temp.view

import com.arch.temp.model.MainClassJson
import com.arch.temp.model.SelectParam
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.components.JBComboBoxLabel
import com.intellij.ui.layout.GrowPolicy
import com.intellij.ui.layout.panel
import javax.swing.*

const val TITLE_DIALOG = "Fill param"

class CreateTemplateDialog(
    private val mainClassJson: MainClassJson,
    project: Project?,
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
            mainClassJson.param.forEach { param  ->
                row(param) {
                    val input = JTextField()
                    mapInput[param] = input
                    input().growPolicy(GrowPolicy.SHORT_TEXT)
                }
            }
            mainClassJson.selectParam.forEach {
                row(it.paramName){
                    val combo = JComboBox(it.paramValue.toTypedArray())
                    mapComboBox[it.paramName] = combo
                    combo().growPolicy(GrowPolicy.MEDIUM_TEXT)
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