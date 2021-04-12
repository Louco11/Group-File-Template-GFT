import com.intellij.openapi.actionSystem.ActionGroup
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.project.impl.ProjectExImpl
import constant.Constants
import mapper.JsonModelMapper
import java.io.File
import java.nio.charset.Charset

class ListTemplate : ActionGroup() {

    override fun getChildren(event: AnActionEvent?): Array<AnAction> {
        val listTemplate = mutableListOf<AnAction>()

        event?.getData(CommonDataKeys.PROJECT)?.let {
            val basePath = "${(it as ProjectExImpl).basePath}${Constants.PATH_TEMPLATE}"
            val dirTemplate = File(basePath)
            if (dirTemplate.isDirectory) {
                dirTemplate.list().forEach { templatePath ->
                    val pathMainFile = "$basePath/$templatePath"
                    val mainFile = File(pathMainFile, Constants.MAIN_FILE_TEMPLATE)
                    if (mainFile.isFile) {
                        try {
                            val classType = JsonModelMapper.mapToMainClassXml(mainFile.readText(Charset.defaultCharset()))
                            listTemplate.add(
                                Dialog(classType)
                            )
                        } catch (e: Exception) {

                        }
                    }

                }
            }
        }

        return listTemplate.toTypedArray()
    }
}