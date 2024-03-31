package com.arch.temp.node

import com.arch.temp.constant.Constants.ExtensionConst.GFT_PROJECT_ID
import com.arch.temp.constant.Constants.ExtensionConst.ROOT_ID
import com.arch.temp.tools.FileTemplateExt.getRootPathTemplate
import com.arch.temp.tools.getBasePathTemplate
import com.intellij.icons.AllIcons
import com.intellij.ide.projectView.PresentationData
import com.intellij.ide.projectView.ProjectViewNode
import com.intellij.ide.projectView.ViewSettings
import com.intellij.ide.projectView.impl.nodes.PsiDirectoryNode
import com.intellij.ide.projectView.impl.nodes.PsiFileNode
import com.intellij.ide.projectView.impl.nodes.PsiFileSystemItemFilter
import com.intellij.ide.scratch.RootType
import com.intellij.ide.scratch.ScratchTreeStructureProvider
import com.intellij.ide.util.treeView.AbstractTreeNode
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Comparing
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiFileSystemItem
import com.intellij.psi.PsiManager
import com.intellij.psi.search.PsiElementProcessor
import java.io.File
import java.util.*

internal class MyRootTemplatesNode(project: Project?, type: RootType, settings: ViewSettings?) :
    ProjectViewNode<RootType?>(project, type, settings), PsiFileSystemItemFilter {
    private val rootType: RootType
        get() = Objects.requireNonNull(value)!!

    override fun contains(file: VirtualFile): Boolean {
        return value!!.containsFile(file)
    }

    override fun getVirtualFile(): VirtualFile? {
        return ScratchTreeStructureProvider.getVirtualFile(rootType)
    }

    override fun getRoots(): Collection<VirtualFile> {
        return getDefaultRootsFor(virtualFile)
    }

    override fun getChildren(): Collection<AbstractTreeNode<*>> {
        return getDirectoryChildrenImpl(
            project,
            directory,
            settings,
            this
        )
    }

    private val directory: PsiDirectory?
        get() {
            val psiManager = PsiManager.getInstance(project)

            return if (rootType.id == ROOT_ID) {
                getRootPdiDirectory(psiManager)
            } else {
                getProjectPsiDirectory(psiManager)
            }
        }

    private fun getProjectPsiDirectory(psiManager: PsiManager): PsiDirectory? {
        val baseDirPath = project.basePath
        val baseDir = if (baseDirPath == null) null else LocalFileSystem.getInstance().findFileByPath(baseDirPath)

        return if (baseDir != null) {
            val templatePath = project.getBasePathTemplate()
            val templateFile = File(templatePath)

            if (templateFile.isDirectory) {
                LocalFileSystem.getInstance().refreshAndFindFileByPath(templatePath)?.let { virtualFile ->
                    psiManager.findDirectory(virtualFile)
                }
            } else {
                null
            }
        } else {
            null
        }
    }

    private fun getRootPdiDirectory(psiManager: PsiManager): PsiDirectory? {
        val rootPath = getRootPathTemplate()

        val templateRootFile = File(rootPath)
        return if (templateRootFile.isDirectory) {
            try {
                LocalFileSystem.getInstance().refreshAndFindFileByPath(rootPath)
                    ?.let { virtualFile ->
                        psiManager.findDirectory(virtualFile)
                    }
            } catch (_ : Throwable) {
                LocalFileSystem.getInstance().findFileByPath(rootPath)
                    ?.let { virtualFile ->
                        psiManager.findDirectory(virtualFile)
                    }
            }
        } else {
            null
        }
    }

    override fun update(presentation: PresentationData) {
        presentation.setIcon(AllIcons.Nodes.Folder)
        presentation.presentableText = rootType.displayName
    }

    override fun canRepresent(element: Any): Boolean {
        return Comparing.equal(directory, element)
    }

    val isEmpty: Boolean
        get() {
            return rootType.id != ROOT_ID && rootType.id != GFT_PROJECT_ID
        }

    override fun shouldShow(item: PsiFileSystemItem): Boolean {
        return !rootType.isIgnored(project, item.virtualFile)
    }

    companion object {

        fun getDirectoryChildrenImpl(
            project: Project,
            directory: PsiDirectory?,
            settings: ViewSettings,
            filter: PsiFileSystemItemFilter
        ): Collection<AbstractTreeNode<*>> {

            val result: MutableList<AbstractTreeNode<*>> = ArrayList()
            val processor = PsiElementProcessor<PsiFileSystemItem> { element ->
                if (element is PsiDirectory) {
                    result.add(object : PsiDirectoryNode(
                        project,
                        element,
                        settings,
                        filter
                    ) {
                        override fun getChildrenImpl(): Collection<AbstractTreeNode<*>> {
                            return getDirectoryChildrenImpl(
                                getProject(),
                                value,
                                getSettings(),
                                getFilter()!!
                            )
                        }
                    })
                } else if (element is PsiFile) {
                    result.add(PsiFileNode(project, element, settings))
                }
                true
            }
            if (directory == null || !directory.isValid) return emptyList()
            directory.processChildren(processor)
            return result
        }
    }
}
