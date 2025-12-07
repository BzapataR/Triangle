package com.bzapata.triangle.intro.paths

import android.net.Uri

sealed interface PathsActions {
    data class SetTrianglePath(val uri : Uri?): PathsActions
    data class SetRomsPath (val uri : Uri?): PathsActions
}