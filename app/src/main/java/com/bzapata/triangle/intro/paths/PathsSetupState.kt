package com.bzapata.triangle.intro.paths

import android.net.Uri

data class PathsSetupState(
    val trianglePath: Uri? = null,
    val romPath: List<Uri> = emptyList()
)
