package com.bzapata.triangle

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class MainViewModel : ViewModel() {
    private val _focusedGame = MutableStateFlow<Int?>(null)
    val focusedGame = _focusedGame.asStateFlow()

    private val _isSettingsOpen = MutableStateFlow(false)
    val isSettingsOpen = _isSettingsOpen.asStateFlow()

    private val _fileMenuOpen = MutableStateFlow(false)
    val fileMenuOpen = _fileMenuOpen.asStateFlow()

    fun onGameFocus(gameIndex: Int?) {
        _focusedGame.value = gameIndex
    }

    fun toggleSettings() {
        _isSettingsOpen.update { !it }
    }

    fun toggleFileMenu() {
        _fileMenuOpen.update { !it }
    }
}
