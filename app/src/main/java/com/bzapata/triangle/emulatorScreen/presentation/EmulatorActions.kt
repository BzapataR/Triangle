package com.bzapata.triangle.emulatorScreen.presentation

import com.bzapata.triangle.emulatorScreen.domain.GameUiExample

sealed interface EmulatorActions {
    data class ToggleGameContextMenu(val gameIndex : Int?) : EmulatorActions
    data class PlayGame(val gameUiExample: GameUiExample) :  EmulatorActions
    data class OnPageChange(val page : Int) : EmulatorActions
    data object ToggleSettings : EmulatorActions
    data object ToggleFileContextMenu : EmulatorActions
}