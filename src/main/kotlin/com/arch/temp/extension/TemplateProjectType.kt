package com.arch.temp.extension

import com.arch.temp.constant.Constants.ExtensionConst.GFT_PROJECT_ID
import com.arch.temp.constant.Constants.ExtensionConst.GFT_PROJECT_NAME
import com.intellij.ide.scratch.RootType

class TemplateProjectType : RootType(GFT_PROJECT_ID, GFT_PROJECT_NAME) {
    val instance: TemplateProjectType
        get() = findByClass(TemplateProjectType::class.java)
}