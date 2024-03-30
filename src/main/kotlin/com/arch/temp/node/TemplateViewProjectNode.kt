package com.arch.temp.node

import com.arch.temp.tools.FileTemplateExt.getRootPathTemplate
import com.arch.temp.tools.TemplateUtils
import com.intellij.ide.projectView.PresentationData
import com.intellij.ide.projectView.ProjectView
import com.intellij.ide.projectView.ProjectViewNode
import com.intellij.ide.projectView.ViewSettings
import com.intellij.ide.projectView.impl.AbstractProjectViewPane
import com.intellij.ide.projectView.impl.ProjectViewPane
import com.intellij.ide.scratch.RootType
import com.intellij.ide.util.treeView.AbstractTreeNode
import com.intellij.openapi.Disposable
import com.intellij.openapi.application.ReadAction
import com.intellij.openapi.extensions.ExtensionPointListener
import com.intellij.openapi.extensions.PluginDescriptor
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Disposer
import com.intellij.openapi.vfs.AsyncFileListener
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.vfs.VirtualFileManager
import com.intellij.openapi.vfs.newvfs.events.VFileCopyEvent
import com.intellij.openapi.vfs.newvfs.events.VFileCreateEvent
import com.intellij.openapi.vfs.newvfs.events.VFileEvent
import com.intellij.openapi.vfs.newvfs.events.VFileMoveEvent
import com.intellij.util.PlatformIcons
import com.intellij.util.RunnableCallable
import com.intellij.util.concurrency.NonUrgentExecutor
import com.intellij.util.containers.ConcurrentFactoryMap
import com.intellij.util.containers.ContainerUtil
import com.intellij.util.containers.JBIterable
import java.io.File
import java.util.*

class TemplateViewProjectNode(
    project: Project,
    viewSettings: ViewSettings
) : ProjectViewNode<Project>(project, project, viewSettings) {

    init {
        registerUpdaters(project, object : Runnable {
            var updateTarget: AbstractProjectViewPane? = null
            override fun run() {
                if (project.isDisposed) return
                if (updateTarget == null) {
                    updateTarget = ProjectView.getInstance(project).getProjectViewPaneById(ProjectViewPane.ID)
                }
                if (updateTarget != null) updateTarget!!.updateFromRoot(true)
            }
        })
    }

    private fun registerUpdaters(project: Project, onUpdate: Runnable) {
        VirtualFileManager.getInstance().addAsyncFileListener({ events: List<VFileEvent?>? ->
            val update = JBIterable.from(events)
                .find { e: VFileEvent? ->
                    ProgressManager.checkCanceled()
                    val parent = getNewParent(e!!)
                    if (TemplateUtils.isTemplate(parent)) return@find true
                    if (!isDirectory(e)) return@find false
                    false
                } != null
            if (!update) null else object : AsyncFileListener.ChangeApplier {
                override fun afterVfsChange() {
                    onUpdate.run()
                }
            }
        }, project)
        val disposables = ConcurrentFactoryMap.createMap { o: RootType ->
            Disposer.newDisposable(
                o.displayName!!
            )
        }
        for (rootType in RootType.getAllRootTypes()) {
            registerRootTypeUpdater(project, rootType, onUpdate, project, disposables)
        }
        RootType.ROOT_EP.addExtensionPointListener(
            object : ExtensionPointListener<RootType?> {
                override fun extensionAdded(extension: RootType?, pluginDescriptor: PluginDescriptor) {
                    registerRootTypeUpdater(project, extension!!, onUpdate, project, disposables)
                }

                override fun extensionRemoved(extension: RootType?, pluginDescriptor: PluginDescriptor) {
                    val rootDisposable = disposables.remove(extension)
                    if (rootDisposable != null) Disposer.dispose(rootDisposable)
                }
            },
            project
        )
        RootType.ROOT_EP.addChangeListener(onUpdate, project)
    }

    private fun registerRootTypeUpdater(
        project: Project,
        rootType: RootType,
        onUpdate: Runnable,
        parentDisposable: Disposable,
        disposables: Map<RootType, Disposable>
    ) {
        if (rootType.isHidden) return
        val rootDisposable = disposables[rootType]
        Disposer.register(parentDisposable, rootDisposable!!)
        ReadAction
            .nonBlocking(RunnableCallable { rootType.registerTreeUpdater(project, parentDisposable, onUpdate) })
            .expireWith(parentDisposable)
            .submit(NonUrgentExecutor.getInstance())
    }

    private fun getNewParent(e: VFileEvent): VirtualFile {
        return when (e) {
            is VFileMoveEvent -> e.newParent
            is VFileCopyEvent -> e.newParent
            is VFileCreateEvent -> e.parent
            else -> Objects.requireNonNull(e.file)!!.parent
        }
    }

    private fun isDirectory(e: VFileEvent): Boolean {
        return if (e is VFileCreateEvent) {
            e.isDirectory
        } else {
            Objects.requireNonNull(e.file)!!.isDirectory
        }
    }

    override fun canRepresent(element: Any?): Boolean {
        val fileClassTemplate = File(getRootPathTemplate())
        return fileClassTemplate.isDirectory
    }

    override fun contains(file: VirtualFile): Boolean {
        return false
    }

    override fun update(presentation: PresentationData) {
        presentation.setIcon(PlatformIcons.PROJECT_ICON)
        presentation.presentableText = myProject.name
    }

    private fun createRootTypeNode(project: Project, rootType: RootType, settings: ViewSettings): AbstractTreeNode<*>? {
        if (rootType.isHidden) return null
        val node = MyRootTemplatesNode(project, rootType, settings)
        return if (node.isEmpty) null else node
    }

    override fun getChildren(): List<AbstractTreeNode<*>> {
        val project = myProject
        if (project == null || project.isDisposed || project.isDefault) {
            return emptyList()
        }

        val nodes = mutableListOf<AbstractTreeNode<*>>()

        for (rootType in RootType.getAllRootTypes()) {
            ContainerUtil.addIfNotNull(
                nodes,
                createRootTypeNode(project, rootType!!, settings)
            )
        }

        return nodes
    }
}