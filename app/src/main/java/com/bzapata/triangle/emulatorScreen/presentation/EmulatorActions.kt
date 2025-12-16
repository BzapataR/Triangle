package com.bzapata.triangle.emulatorScreen.presentation

import android.net.Uri
import com.bzapata.triangle.emulatorScreen.domain.Game

sealed interface EmulatorActions {
    data class ToggleGameContextMenu(val gameHash: String? ) : EmulatorActions
    data class PlayGame(val game: Game) : EmulatorActions
    data class OnPageChange(val page: Int) : EmulatorActions
    data object ToggleSettings : EmulatorActions
    data object ToggleFileContextMenu : EmulatorActions
    data class SetUserFolder(val uri: Uri?) : EmulatorActions
    data class SetRomsFolder(val uri: Uri?) : EmulatorActions
    data object RefreshRomList : EmulatorActions
}
