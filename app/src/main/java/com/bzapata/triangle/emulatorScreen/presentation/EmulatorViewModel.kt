package com.bzapata.triangle.emulatorScreen.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bzapata.triangle.data.repository.ConfigRepository
import com.bzapata.triangle.emulatorScreen.data.GameRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EmulatorViewModel(
    private val gameRepo: GameRepository,
    private val configRepo: ConfigRepository
) : ViewModel() {

    private val _state = MutableStateFlow(EmulatorState())
    val state = _state.asStateFlow()

    init {
        setScreen()
        observeRomPath()
    }
    private fun setScreen() {
        gameRepo.getGames().distinctUntilChanged()
            .onEach { games ->
                _state.update { currentState ->
                    val updatedConsoles = games.map { it.consoles }.distinct().sorted()
                    currentState.copy(games = games.sortedBy { it.name }, consoles = updatedConsoles)
                }
            }.launchIn(viewModelScope)
    }
    private fun observeRomPath() {
        configRepo.romUriFlow.distinctUntilChanged().onEach { uri ->
            if (uri != null && !_state.value.isInitialScanDone) {
                CoroutineScope(Dispatchers.IO).launch {
                    _state.update { it.copy(noRomPath = false, isScanning = true)}
                    gameRepo.scanRoms()
                    _state.update { it.copy(isInitialScanDone = true, isScanning = false) }
                }
            }
            else if(uri==null){
                _state.update { it.copy(noRomPath = true) }
            }
            else {
                _state.update { it.copy(noRomPath = false) }
            }
        }.launchIn(viewModelScope)
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
                        gameHashForContextMenu = action.gameHash,
                        isBackgroundBlurred =  !it.isBackgroundBlurred
                    )
                }
            }

            is EmulatorActions.ToggleSettings -> {
                _state.update {
                    it.copy(
                        isSettingsOpen = !it.isSettingsOpen
                    )
                }
            }

            is EmulatorActions.ToggleFileContextMenu -> {
                _state.update { currentState ->
                    val newFileMenuState = !currentState.isFileContextMenuOpen
                    currentState.copy(
                        isFileContextMenuOpen = newFileMenuState,
                        isBackgroundBlurred = newFileMenuState || currentState.gameHashForContextMenu != null
                    )
                }
            }

            is EmulatorActions.SetUserFolder -> {
                viewModelScope.launch {
                    action.uri?.let { configRepo.saveTriangleDataUri(it) }
                }
            }

            is EmulatorActions.SetRomsFolder -> {
                CoroutineScope(Dispatchers.IO).launch {
                    action.uri?.let { configRepo.saveRomsUri(it) }
                    gameRepo.databaseBomb()
                    _state.update { it.copy(isScanning = true) }
                    gameRepo.scanRoms()
                    _state.update { it.copy(isScanning = false) }
                }
            }

            is EmulatorActions.PlayGame -> {}
            EmulatorActions.RefreshRomList -> {
                CoroutineScope(Dispatchers.IO).launch {
                    _state.update { it.copy(isRefreshing = true) }
                    gameRepo.scanRoms()
                    _state.update { it.copy(isRefreshing = false)}
                }
            }
        }
    }

}
