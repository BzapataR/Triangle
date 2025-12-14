package com.bzapata.triangle.emulatorScreen.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bzapata.triangle.data.repository.ConfigRepository
import com.bzapata.triangle.emulatorScreen.data.GameRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EmulatorViewModel(
    gameRepo: GameRepository,
    private val configRepo: ConfigRepository
) : ViewModel() {

    private val _state = MutableStateFlow(EmulatorState())
    val state = _state.asStateFlow()

    init {
        gameRepo.readAllGames()
            .onEach { games ->
                _state.update { currentState ->
                    val updatedConsoles = games.map { it.consoles }.distinct().sorted()
                    currentState.copy(games = games, consoles = updatedConsoles)
                }
                Log.i("Emulator ViewModel", "Games updated: ${games.size}")
            }
            .launchIn(viewModelScope)
    }

    fun onAction(action: EmulatorActions) {
        when (action) {
            is EmulatorActions.OnPageChange -> {
                _state.update {
                    it.copy(
                        currentPage = action.page
                    )
                }
                Log.i("Page", "Current Page: ${_state.value}")
            }

            is EmulatorActions.ToggleGameContextMenu -> {
                _state.update {
                    it.copy(
                        gameIndexForContextMenu = action.gameIndex,
                        isBackgroundBlurred = !it.isBackgroundBlurred
                    )
                }
            }

            is EmulatorActions.ToggleSettings -> {
                _state.update {
                    it.copy(
                        isSettingsOpen = !_state.value.isSettingsOpen
                    )
                }
            }

            is EmulatorActions.ToggleFileContextMenu -> {
                _state.update {
                    val newFileMenuState = !it.isFileContextMenuOpen
                    it.copy(
                        isFileContextMenuOpen = newFileMenuState,
                        isBackgroundBlurred = newFileMenuState || it.gameIndexForContextMenu != null
                    )
                }
            }

            is EmulatorActions.SetUserFolder -> {
                viewModelScope.launch {
                    action.uri?.let { configRepo.saveTriangleDataUri(it) }
                }
            }

            is EmulatorActions.SetRomsFolder -> {
                viewModelScope.launch {
                    action.uri?.let { configRepo.saveRomsUri(it) }
                }
            }

            is EmulatorActions.PlayGame -> {}
        }
    }

}
