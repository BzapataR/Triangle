package com.bzapata.triangle.emulatorScreen.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bzapata.triangle.emulatorScreen.data.GameRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class EmulatorViewModel(
    private val gameRepo: GameRepository,
) : ViewModel() {
    private var getGames : Job? = null

    private val _state = MutableStateFlow(EmulatorState())
    val state = _state.asStateFlow()
        .onStart { getRoms() }
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            _state.value
        )

    private fun getRoms() {
        getGames?.cancel()
        _state.update { it.copy(games = emptyList(), consoles = emptyList()) } // Reset consoles as well
        getGames = gameRepo.readAllGames()
            .onEach { game ->
                _state.update { currentState ->
                    val updatedGames = (currentState.games + game).sortedBy { it.name }
                    val updatedConsoles = updatedGames.map { it.consoles }.distinct().sorted()
                    currentState.copy(games = updatedGames, consoles = updatedConsoles)
                }
                Log.i("Emulator ViewModel", "Games updated: ${_state.value.games.size}")
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

            is EmulatorActions.PlayGame -> {}
        }
    }

}
