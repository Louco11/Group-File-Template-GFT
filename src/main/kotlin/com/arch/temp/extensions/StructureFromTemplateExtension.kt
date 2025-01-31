package com.arch.temp.extensions

/**
 * Interface defining the structure for template extension callback.
 */
interface StructureFromTemplateExtension {

    /**
     * Called before creating a template.
     *
     * @param templateName The name of the template.
     * @param templateDescription The description of the template.
     * @param selectedPath The path where the template will be created.
     * @param pathToTemplate The path to the template.
     * @param mapParam Additional parameters for the template creation.
     */
    fun onBeforeCreateTemplate(
        templateName: String,
        templateDescription: String,
        selectedPath: String,
        pathToTemplate: String,
        mapParam: Map<String, String>
    )

    /**
     * Called before creating a file from a template.
     *
     * @param selectedPath The path where the file will be created.
     * @param mapParam Additional parameters for the file creation.
     * @param pathToTemplate The path to the template.
     * @param pathToTemplateFile The path to the template file.
     * @param fileText The text content of the file. All variables are not replaced.
     *
     * @return The modified text content of the file.
     */
    fun onBeforeCreateFile(
        selectedPath: String,
        mapParam: Map<String, String>,
        pathToTemplate: String,
        pathToTemplateFile: String,
        fileText: String
    ): String

    /**
     * Called after creating a file from a template.
     *
     * @param selectedPath The path where the file was created.
     * @param mapParam Additional parameters for the file creation.
     * @param pathToTemplate The path to the template.
     * @param pathToTemplateFile The path to the template file.
     * @param fileText The text content of the file. All variables are already replaced.
     *
     * @return The modified text content of the file.
     */
    fun onAfterCreateFile(
        selectedPath: String,
        mapParam: Map<String, String>,
        pathToTemplate: String,
        pathToTemplateFile: String,
        fileText: String
    ): String

    /**
     * Called after creating a template.
     *
     * @param templateName The name of the template.
     * @param templateDescription The description of the template.
     * @param selectedPath The path where the template was created.
     * @param pathToTemplate The path to the template.
     * @param mapParam Additional parameters for the template creation.
     */
    fun onAfterCreateTemplate(
        templateName: String,
        templateDescription: String,
        selectedPath: String,
        pathToTemplate: String,
        mapParam: Map<String, String>
    )
}