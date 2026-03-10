package com.bzapata.triangle.settings.Paths

import android.net.Uri

data class PathsState(
    val trianglePath: Uri? = null,
    val romPaths: List<Uri> = emptyList()
)
