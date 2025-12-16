package com.bzapata.triangle.emulatorScreen.presentation

import com.bzapata.triangle.emulatorScreen.domain.Consoles
import com.bzapata.triangle.emulatorScreen.domain.Game

data class EmulatorState(
    val consoles: List<Consoles> = emptyList(),
    val games: List<Game> = emptyList(),
    val gameHashForContextMenu: String? = null,
    val isFileContextMenuOpen: Boolean = false,
    val isBackgroundBlurred: Boolean = false,
    val currentPage: Int = 0,
    val isSettingsOpen: Boolean = false,
    val isRefreshing : Boolean = false,
    val isInitialScanDone : Boolean = false,
    val noRomPath : Boolean = false,
    val isScanning : Boolean = false
)
