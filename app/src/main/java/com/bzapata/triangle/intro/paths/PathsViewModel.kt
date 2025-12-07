package com.bzapata.triangle.intro.paths

import android.net.Uri
import android.util.Log
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bzapata.triangle.data.repository.ConfigRepository
import com.bzapata.triangle.fileOperations.directoryPicker
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PathsViewModel(
    private val configRepo : ConfigRepository
) : ViewModel()  {

    private var observerConfig : Job? = null
    private val _state = MutableStateFlow(PathsState())

    val state = _state.asStateFlow()
        .onStart {
            fetchConfig()
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            _state.value
        )

    private fun fetchConfig() {
        observerConfig?.cancel()
        observerConfig = combine(
            flow = configRepo.triangleDataUriFlow,
            flow2 = configRepo.romUriFlow
        ){ trianglePath, romPath ->
            _state.update {
                it.copy(
                    trianglePath = trianglePath,
                    romPath = romPath
                )
            }

        }.launchIn(viewModelScope)
    }

    fun onAction(actions: PathsActions) {
        when (actions) {
            is PathsActions.SetRomsPath -> {
                setRomPath(actions.uri)
            }
            is PathsActions.SetTrianglePath -> {
                setTrianglePath(actions.uri)
            }
        }
        Log.i("Paths", "rom path: ${_state.value.romPath}, triangle path: ${_state.value.trianglePath}")
    }

    private fun setTrianglePath(uri: Uri?) {
        viewModelScope.launch {
            configRepo.saveTriangleDataUri(uri)
        }
    }

    private fun setRomPath(uri: Uri?) {
        viewModelScope.launch {
            configRepo.saveRomsUri(uri)
        }
    }
}