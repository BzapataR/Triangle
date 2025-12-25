package com.bzapata.triangle.emulatorScreen.presentation

import android.net.Uri
import com.bzapata.triangle.emulatorScreen.domain.Game

sealed interface EmulatorActions {
    data class ToggleGameContextMenu(val gameHash: String?) : EmulatorActions
    data class SelectGame(val game : Game?) : EmulatorActions
    data class PlayGame(val game: Game) : EmulatorActions
    data class OnPageChange(val page: Int) : EmulatorActions
    data object ToggleSettings : EmulatorActions
    data object ToggleFileContextMenu : EmulatorActions
    data class SetUserFolder(val uri: Uri?) : EmulatorActions
    data class SetRomsFolder(val uri: Uri?) : EmulatorActions
    data object RefreshRomList : EmulatorActions
    data object ToggleCoverActionSheet : EmulatorActions
    data object ToggleDbCover : EmulatorActions
    data class QueryCovers(val gameName: String) : EmulatorActions
    data class SaveCover(val uri: Uri, val gameHash : String) : EmulatorActions
    data class SaveCoverFromClipboard(val gameHash: String) : EmulatorActions
    data object ClearError : EmulatorActions
    data class LaunchExternalRom(val uri: Uri) : EmulatorActions
    data object ToggleRenameDialog : EmulatorActions
    data class RenameRom(val newName : String) : EmulatorActions
}
