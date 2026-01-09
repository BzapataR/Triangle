package com.bzapata.triangle.emulatorScreen.presentation

import android.util.Log
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bzapata.triangle.data.repository.ConfigRepository
import com.bzapata.triangle.emulatorScreen.data.GameRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EmulatorViewModel(
    private val gameRepo: GameRepository,
    private val configRepo: ConfigRepository
) : ViewModel() {

    private val _state = MutableStateFlow(EmulatorState())
    val state = _state.asStateFlow()

    private var searchCoversJob: Job? = null
    private var searchRomsJob : Job? = null

    init {
        setScreen()
        observeRomPath()
        observeRomQuery()
        observeCoverSearchQuery()
    }

    private fun setScreen() {
        gameRepo.getGames().distinctUntilChanged()
            .onEach { games ->
                _state.update { currentState ->
                    val updatedConsoles = games.map { it.consoles }.distinct().sorted()
                    currentState.copy(
                        games = games.sortedBy { it.name },
                        consoles = updatedConsoles,
                    )
                }
            }.launchIn(viewModelScope)
    }

    private fun observeRomPath() {
        configRepo.romUriFlow.distinctUntilChanged().onEach { uri ->
            if (uri != null && !_state.value.isInitialScanDone) {
                CoroutineScope(Dispatchers.IO).launch {
                    _state.update { it.copy(noRomPath = false, isScanning = true) }
                    gameRepo.scanRoms()
                    _state.update { it.copy(isInitialScanDone = true, isScanning = false) }
                }
            } else if (uri == null) {
                _state.update { it.copy(noRomPath = true) }
            } else {
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
                Log.i("Page", "Current Page: ${_state.value.currentPage}")
            }

            is EmulatorActions.ToggleGameContextMenu -> {
                _state.update {
                    it.copy(
                        gameHashForContextMenu = action.gameHash,
                        isBackgroundBlurred = !it.isBackgroundBlurred
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
                    currentState.copy(
                        isFileContextMenuOpen = !currentState.isFileContextMenuOpen,
                        isBackgroundBlurred = !currentState.isBackgroundBlurred
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
                    _state.update { it.copy(isScanning = true, currentPage = 0) }
                    gameRepo.scanRoms()
                    _state.update { it.copy(isScanning = false) }
                }
            }

            is EmulatorActions.PlayGame -> {
                // Handle playing the game
            }

            is EmulatorActions.RefreshRomList -> {
                CoroutineScope(Dispatchers.IO).launch {
                    _state.update { it.copy(isRefreshing = true) }
                    gameRepo.scanRoms()
                    _state.update { it.copy(isRefreshing = false) }
                }
            }

            is EmulatorActions.ToggleCoverActionSheet -> {
                _state.update { it.copy(isCoverActionSheetOpen = !it.isCoverActionSheetOpen) }
            }

            is EmulatorActions.QueryCovers -> {
                    _state.update { it.copy(coverQuery = action.gameName) }
            }

            is EmulatorActions.ToggleDbCover -> {
                _state.update { it.copy(isCoverDbSelectorOpen = !it.isCoverDbSelectorOpen) }
            }

            is EmulatorActions.SelectGame -> {
                _state.update { it.copy(selectedGame = action.game) }
            }

            is EmulatorActions.SaveCover -> {
                viewModelScope.launch {
                    gameRepo.saveCover(action.uri, action.gameHash)
                }
            }

            is EmulatorActions.SaveCoverFromClipboard -> {
                viewModelScope.launch {
                    try {
                        gameRepo.getCoverFromClipboard(action.gameHash)
                    } catch (e: Exception) {
                        _state.update { it.copy(errorMessage = e.message) }
                    }
                }
            }

            is EmulatorActions.ClearError -> {
                _state.update { it.copy(errorMessage = null) }
            }

            is EmulatorActions.LaunchExternalRom -> {
                Log.i("EmulatorViewModel", "External ROM launch requested: ${action.uri}")
                viewModelScope.launch {
                    gameRepo.addSingleGame(action.uri)
                }
                // Logic to handle external ROM launch:
                // 1. Identify the ROM (maybe use idRom logic)
                // 2. Start the appropriate emulator
            }

            is EmulatorActions.RenameRom -> {
                viewModelScope.launch {
                    gameRepo.renameRom(
                        action.newName,
                        gameHash = _state.value.selectedGame?.hash ?: return@launch
                    )
                }
            }

            is EmulatorActions.ToggleRenameDialog -> {
                _state.update { it.copy(renameDialogOpen = !it.renameDialogOpen) }
            }
            is EmulatorActions.QuerySavedRoms -> {
                _state.update { it.copy(romQuery = action.name) }
            }
            is EmulatorActions.MovePage -> {
                _state.update { currentState ->
                    val maxPage = currentState.consoles.size -1
                    if (maxPage < 0)
                        return@update currentState

                    val newPage = when (action.offset) {
                        Int.MAX_VALUE -> maxPage
                        Int.MIN_VALUE -> 0
                        else -> {
                            val calculatedPage = currentState.currentPage + action.offset
                                calculatedPage.coerceIn(0, maxPage)
                        }
                    }
                    currentState.copy(currentPage = newPage)
                }
            }
            is EmulatorActions.FocusSearchBar -> {}
        }
    }
    fun changeWindowSize(windowSize : WindowWidthSizeClass) {
        _state.update { it.copy(windowSize = windowSize) }
    }

    @OptIn(FlowPreview::class)
    private fun observeCoverSearchQuery() {
        _state.map { it.coverQuery }
            .distinctUntilChanged()
            .debounce(500L)
            .onEach { query ->
                when {
                    query.isBlank() -> {
                        _state.update { it.copy(queriedCovers = emptyMap()) }
                    }

                    query.length >= 2 -> {
                        searchCoversJob?.cancel()
                        searchCoversJob = searchCovers(query)
                    }
                }
            }.launchIn(viewModelScope)
    }

    private fun searchCovers(query: String) = viewModelScope.launch {
        _state.update { it.copy(queriedCovers = gameRepo.queryCovers(query)) }
    }

    private fun observeRomQuery() {
        _state.map { it.romQuery }
            .distinctUntilChanged()
            .onEach { query->
                when{
                    query.isBlank() -> {
                        _state.update { it.copy(queriedRoms = emptyList()) }
                    }
                    else -> {
                        searchRomsJob?.cancel()
                        searchRomsJob = searchRoms(query)
                    }
                }
            }.launchIn(viewModelScope)
    }
    private fun searchRoms(query : String) = viewModelScope.launch{
        _state.update { it.copy(queriedRoms = gameRepo.querySavedRoms(query)) }
    }
}
