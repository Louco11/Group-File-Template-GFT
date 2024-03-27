package com.arch.temp.extension

import com.arch.temp.constant.Constants.ExtensionConst.GFT_ROOT_ID
import com.arch.temp.constant.Constants.ExtensionConst.GFT_ROOT_NAME
import com.intellij.ide.scratch.RootType

class TemplateRootType : RootType(GFT_ROOT_ID, GFT_ROOT_NAME) {
    val instance: TemplateRootType
        get() = findByClass(TemplateRootType::class.java)
}