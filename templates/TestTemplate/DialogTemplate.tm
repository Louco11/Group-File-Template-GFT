package {package}.dialog.{namePath}

import com.arch.temp.model.MainClassJson
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.layout.panel
import javax.swing.JComponent

class Dialog{name}Template(
    private val param: List<MainClassJson>,
    project: Project?,
) : DialogWrapper(project) {

    init {
        init()
    }

    override fun createCenterPanel(): JComponent {
        return panel {
        }
    }

    override fun doOKAction() {
        super.doOKAction()
    }

}