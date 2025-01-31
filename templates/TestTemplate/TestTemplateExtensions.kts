import com.arch.temp.extensions.StructureFromTemplateExtension
import java.nio.file.Files
import java.nio.file.Path

/**
 * Sample extension for StructureFromTemplateExtension
 * Just logs the parameters to a file.
 */
object : StructureFromTemplateExtension {

    override fun onBeforeCreateTemplate(
        templateName: String,
        templateDescription: String,
        selectedPath: String,
        pathToTemplate: String,
        mapParam: Map<String, String>
    ) {
        val result = listOf<String>()
            .plus(templateName)
            .plus(templateDescription)
            .plus(selectedPath)
            .plus(pathToTemplate)
            .plus(mapParam)
            .joinToString(" ///\n")
        Files.writeString(Path.of(selectedPath, "onBeforeCreateTemplate.txt"), result)
    }

    override fun onBeforeCreateFile(
        selectedPath: String,
        mapParam: Map<String, String>,
        pathToTemplate: String,
        pathToTemplateFile: String,
        fileText: String
    ): String {
        val result = listOf<String>()
            .plus(selectedPath)
            .plus(mapParam)
            .plus(pathToTemplate)
            .plus(pathToTemplateFile)
            .plus(fileText)
            .joinToString(" ///\n")
        Files.writeString(Path.of(selectedPath, "onBeforeCreateFile.txt"), result)
        return fileText
    }

    override fun onAfterCreateFile(
        selectedPath: String,
        mapParam: Map<String, String>,
        pathToTemplate: String,
        pathToTemplateFile: String,
        fileText: String
    ): String {
        val result = listOf<String>()
            .plus(selectedPath)
            .plus(mapParam)
            .plus(pathToTemplate)
            .plus(pathToTemplateFile)
            .plus(fileText)
            .joinToString(" ///\n")
        Files.writeString(Path.of(selectedPath, "onAfterCreateFile.txt"), result)
        return fileText
    }

    override fun onAfterCreateTemplate(
        templateName: String,
        templateDescription: String,
        selectedPath: String,
        pathToTemplate: String,
        mapParam: Map<String, String>
    ) {
        val result = listOf<String>()
            .plus(templateName)
            .plus(templateDescription)
            .plus(selectedPath)
            .plus(pathToTemplate)
            .plus(mapParam)
            .joinToString(" ///\n")
        Files.writeString(Path.of(selectedPath, "onAfterCreateTemplate.txt"), result)
    }
}