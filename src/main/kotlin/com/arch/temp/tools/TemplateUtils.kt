package com.arch.temp.tools

import com.arch.temp.constant.Constants.TEMPLATE
import com.intellij.openapi.vfs.VirtualFile

class TemplateUtils {
    companion object {
        fun isTemplate(file: VirtualFile?): Boolean {
            return file?.let {
                it.name == TEMPLATE
            } ?: false
        }
    }
}