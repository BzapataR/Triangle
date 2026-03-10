package com.bzapata.triangle.settings.Paths

import android.net.Uri

sealed interface PathsActions {
    data class SetRomsPath(val uri: Uri?) : PathsActions
    data class SetTrianglePath(val uri: Uri?) : PathsActions
}
