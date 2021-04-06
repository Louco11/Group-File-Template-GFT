package createEmptyTemplate

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.project.impl.ProjectExImpl
import constant.Constants
import java.io.File
import java.io.StringWriter
import java.util.logging.XMLFormatter
import java.awt.print.Book

import javax.xml.bind.JAXBContext
import javax.xml.bind.Marshaller


class CreateEmptyTemplate : AnAction() {

    override fun actionPerformed(event: AnActionEvent) {
        val basePath = (event.getData(CommonDataKeys.PROJECT) as ProjectExImpl).basePath
        val pathTemplate = File("$basePath${Constants.PATH_TEMPLATE}")
        createPath(
            if (pathTemplate.isDirectory) "${pathTemplate.path}${Constants.EMPTY_TEMPLATE_PATH_NAME}${pathTemplate.list().size}"
            else "${pathTemplate.path}${Constants.EMPTY_TEMPLATE_PATH_NAME}"
        )
    }

    private fun createPath(path: String) {
        File(path).mkdir()
        createMainFileTemplate(path)
    }

    private fun createMainFileTemplate(path: String) {
        File(path, Constants.MAIN_FILE_TEMPLATE).createNewFile()
        val template = EmptyMainClassXml("EmptyTemplate", "Empty Template Description")
        val context = JAXBContext.newInstance(EmptyMainClassXml::class.java)
        val mar = context.createMarshaller()
        mar.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true)
        mar.marshal(template, File(path, Constants.MAIN_FILE_TEMPLATE))


    }


}