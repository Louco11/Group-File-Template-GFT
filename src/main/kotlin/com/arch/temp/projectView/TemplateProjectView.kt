package com.arch.temp.projectView

import com.arch.temp.node.TemplateViewProjectNode
import com.intellij.icons.AllIcons
import com.intellij.ide.SelectInTarget
import com.intellij.ide.impl.ProjectViewSelectInTarget
import com.intellij.ide.projectView.*
import com.intellij.ide.projectView.impl.AbstractProjectViewPaneWithAsyncSupport
import com.intellij.ide.projectView.impl.ProjectAbstractTreeStructureBase
import com.intellij.ide.projectView.impl.ProjectTreeStructure
import com.intellij.ide.projectView.impl.ProjectViewTree
import com.intellij.ide.util.treeView.AbstractTreeNode
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFileSystemItem
import javax.swing.tree.DefaultTreeModel

private const val GFT_HOME_TEMPLATES = "GFT Templates"

private const val TEMPLATE_VIEW_ID = "TemplateView"

class TemplateProjectView(project: Project) : AbstractProjectViewPaneWithAsyncSupport(project) {
    override fun getTitle() = GFT_HOME_TEMPLATES

    override fun getIcon() = AllIcons.General.ProjectTab

    override fun getId() = TEMPLATE_VIEW_ID

    override fun getWeight() = 111

    override fun createSelectInTarget(): SelectInTarget {
        return object : ProjectViewSelectInTarget(myProject) {
            override fun toString(): String {
                return title
            }

            override fun getMinorViewId(): String {
                return id
            }

            override fun getWeight(): Float {
                return this@TemplateProjectView.getWeight().toFloat()
            }

            override fun canSelect(file: PsiFileSystemItem): Boolean {
                return super.canSelect(file)
            }
        }
    }

    override fun createStructure(): ProjectAbstractTreeStructureBase {
        return ProjectViewPaneTreeStructure()
    }

    override fun createTree(treeModel: DefaultTreeModel): ProjectViewTree {
        return object : ProjectViewTree(treeModel) {
            override fun toString(): String {
                return title + " " + super.toString()
            }
        }
    }

    inner class ProjectViewPaneTreeStructure : ProjectTreeStructure(myProject, id), ProjectViewSettings {
        override fun createRoot(project: Project, settings: ViewSettings): AbstractTreeNode<*> {
            return TemplateViewProjectNode(project, settings)
        }

        override fun isShowLibraryContents() = false
    }
}