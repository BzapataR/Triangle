package com.bzapata.triangle.intro.paths

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bzapata.triangle.data.repository.ConfigRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PathsSetupViewModel(
    private val configRepo: ConfigRepository
) : ViewModel() {

    private var observerConfig: Job? = null
    private val _state = MutableStateFlow(PathsSetupState())

    val state = _state.asStateFlow()
        .onStart {
            fetchPaths()
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            _state.value
        )

    private fun fetchPaths() {
        observerConfig?.cancel()
        observerConfig = combine(
            flow = configRepo.triangleDataUriFlow,
            flow2 = configRepo.romUrisFlow
        ) { trianglePath, romPath ->
            _state.update {
                it.copy(
                    trianglePath = trianglePath,
                    romPath = romPath
                )
            }

        }.launchIn(viewModelScope)
    }

    fun onAction(actions: PathsSetupActions) {
        when (actions) {
            is PathsSetupActions.SetRomsPath -> {
                setRomPath(actions.uri)
            }

            is PathsSetupActions.SetTrianglePath -> {
                setTrianglePath(actions.uri)
            }
        }
        Log.i(
            "Paths",
            "rom path: ${_state.value.romPath}, triangle path: ${_state.value.trianglePath}"
        )
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