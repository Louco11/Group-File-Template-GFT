package com.arch.temp.extension

import com.arch.temp.constant.Constants.ExtensionConst.GFT_PROJECT_ID
import com.arch.temp.constant.Constants.ExtensionConst.GFT_PROJECT_NAME
import com.intellij.ide.scratch.RootType

class GFTemplateProjectType : RootType(GFT_PROJECT_ID, GFT_PROJECT_NAME) {
    val instance: GFTemplateProjectType
        get() = findByClass(GFTemplateProjectType::class.java)
}