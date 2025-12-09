package com.bzapata.triangle.emulatorScreen.presentation

import androidx.lifecycle.ViewModel
import com.bzapata.triangle.data.repository.ConfigRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class EmulatorViewModel(
    config: ConfigRepository
) : ViewModel() {

    private val _state = MutableStateFlow(EmulatorState())
    var state = _state.asStateFlow()

    fun onAction(action : EmulatorActions) {
        val previousState = _state.value
        when(action) {
            is EmulatorActions.OnPageChange -> {
                _state.update {
                    it.copy(
                        currentPage = action.page
                    )
                }
            }
            is EmulatorActions.ToggleGameContextMenu -> {
                _state.update {
                    it.copy(
                        isBackgroundBlurred = !previousState.isBackgroundBlurred,
                        isGameContextMenuOpen = !_state.value.isGameContextMenuOpen
                    )
                }
            }
            is EmulatorActions.ToggleSettings -> {
                _state.update{
                    it.copy(
                        isSettingsOpen = !_state.value.isSettingsOpen
                    )
                }
            }
            is EmulatorActions.ToggleFileContextMenu -> {
                _state.update {
                    it.copy(
                        isFileContextMenuOpen = !_state.value.isFileContextMenuOpen,
                        isBackgroundBlurred = !_state.value.isBackgroundBlurred
                    )
                }
            }
            else -> {}
        }
    }

}