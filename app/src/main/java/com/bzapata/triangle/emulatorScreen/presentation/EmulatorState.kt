package com.bzapata.triangle.emulatorScreen.presentation

import com.bzapata.triangle.emulatorScreen.domain.Consoles
import com.bzapata.triangle.emulatorScreen.domain.Game
import com.bzapata.triangle.emulatorScreen.domain.GameUiExample

data class EmulatorState(
    val consoles: List<Consoles> = Consoles.entries.toList(),
    val games : List<Game> = emptyList(),
    val gameIndexForContextMenu : Int? = null,
    val isFileContextMenuOpen: Boolean = false,
    val isBackgroundBlurred : Boolean = false,
    val currentPage: Int = 0,
    val isSettingsOpen : Boolean = false,
    )