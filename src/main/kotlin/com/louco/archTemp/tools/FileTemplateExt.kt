package com.louco.archTemp.tools

import com.louco.archTemp.model.FileTemplate

fun FileTemplate.getPath() = if(path.isNotEmpty() && path.toCharArray().first() == '/') path.removeRange(0,1) else path