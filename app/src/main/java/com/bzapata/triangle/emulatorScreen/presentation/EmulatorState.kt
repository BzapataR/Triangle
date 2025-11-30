package com.bzapata.triangle.emulatorScreen.presentation

import com.bzapata.triangle.emulatorScreen.domain.Consoles
import com.bzapata.triangle.emulatorScreen.domain.Game

data class EmulatorState(
    val consoles: List<Consoles> = Consoles.entries.toList(),
    val games : Map<Game,Consoles> = emptyMap(),
    val isGameContextMenuOpen : Boolean = false,
    val isFileContextMenuOpen: Boolean = false,
    val isBackgroundBlurred : Boolean = false,
    val currentPage: Int = 0,
    val isSettingsOpen : Boolean = false,
    )