package com.bzapata.triangle.settings.Paths

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bzapata.triangle.data.repository.ConfigRepository
import com.bzapata.triangle.emulatorScreen.data.GameRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PathsViewModel(
    private val configRepo: ConfigRepository,
    private val gameRepo: GameRepository
) : ViewModel() {

    private val _state = MutableStateFlow(PathsState())
    val state  = _state.asStateFlow()
    init {
        configRepo.romUrisFlow.distinctUntilChanged().onEach { romPaths->
            _state.update { it.copy(romPaths = romPaths) }
        }.launchIn(viewModelScope)
        configRepo.triangleDataUriFlow.distinctUntilChanged().onEach { trianglePath->
            _state.update { it.copy(trianglePath = trianglePath) }
        }.launchIn(viewModelScope)
    }

    fun onAction(action: PathsActions) {
        when (action) {
            is PathsActions.SetRomsPath -> setRomPath(action.uri)
            is PathsActions.SetTrianglePath -> setTrianglePath(action.uri)
            is PathsActions.OpenContextMenu -> {
                _state.update {
                    it.copy(menuIndex = action.index)
                }
            }
            is PathsActions.RemovePath -> removeRomPath(action.uri, action.context)
        }
    }
    private fun removeRomPath(uri: Uri, context : Context) { //todo remove all roms in path from roomdb
        viewModelScope.launch {
            try {
                context.contentResolver.releasePersistableUriPermission(
                    uri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                )
            }
            catch (e : Exception) {
                Log.e("Paths", "Error releasing Path", e)
            }
            configRepo.removeRomPath(uri)
        }
    }

    private fun setTrianglePath(uri: Uri?) {
        viewModelScope.launch {
            configRepo.saveTriangleDataUri(uri)
        }
    }

    private fun setRomPath(uri: Uri?) {
        viewModelScope.launch {
            configRepo.saveRomsUri(uri)
            if (uri != null) {
                gameRepo.scanRoms()
            }
        }
    }
}
