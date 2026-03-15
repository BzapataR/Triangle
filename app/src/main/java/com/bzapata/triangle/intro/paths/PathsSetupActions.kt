package com.bzapata.triangle.intro.paths

import android.net.Uri

sealed interface PathsSetupActions {
    data class SetTrianglePath(val uri: Uri?) : PathsSetupActions
    data class SetRomsPath(val uri: Uri?) : PathsSetupActions
}