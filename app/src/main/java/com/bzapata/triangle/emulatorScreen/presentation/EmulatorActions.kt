package com.bzapata.triangle.emulatorScreen.presentation

import com.bzapata.triangle.emulatorScreen.domain.Game

sealed interface EmulatorActions {
    data object ToggleGameContextMenu : EmulatorActions
    data class PlayGame(val game: Game) :  EmulatorActions
    data class OnPageChange(val page : Int) : EmulatorActions
    data object ToggleSettings : EmulatorActions
    data object ToggleFileContextMenu : EmulatorActions
}