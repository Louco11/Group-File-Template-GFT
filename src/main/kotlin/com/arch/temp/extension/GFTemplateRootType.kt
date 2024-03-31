package com.arch.temp.extension

import com.arch.temp.constant.Constants.ExtensionConst.ROOT_ID
import com.arch.temp.constant.Constants.ExtensionConst.GFT_ROOT_NAME
import com.intellij.ide.scratch.RootType

class GFTemplateRootType : RootType(ROOT_ID, GFT_ROOT_NAME) {
    val instance: GFTemplateRootType
        get() = findByClass(GFTemplateRootType::class.java)
}
