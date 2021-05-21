package com.arch.temp.tools

import com.arch.temp.model.FileTemplate

fun FileTemplate.getPath() = if(path.isNotEmpty() && path.toCharArray().first() == '/') path.removeRange(0,1) else path