package com.bzapata.triangle.settings.Paths

import android.content.Context
import android.net.Uri

sealed interface PathsActions {
    data class SetRomsPath(val uri: Uri?) : PathsActions
    data class SetTrianglePath(val uri: Uri?) : PathsActions
    data class OpenContextMenu(val index : Int) : PathsActions
    data class RemovePath(val uri : Uri, val context : Context) : PathsActions
}
