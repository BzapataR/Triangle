package com.bzapata.triangle.settings.Paths

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bzapata.triangle.data.repository.ConfigRepository
import com.bzapata.triangle.emulatorScreen.data.GameRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PathsViewModel(
    private val configRepo: ConfigRepository,
    private val gameRepo: GameRepository
) : ViewModel() {

    val state = combine(
        configRepo.triangleDataUriFlow,
        configRepo.romUrisFlow
    ) { trianglePath, romPath -> // todo resume: fix naming

        PathsState(
            trianglePath = trianglePath,
            romPaths = romPath
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000L),
        PathsState()
    )

    fun onAction(action: PathsActions) {
        when (action) {
            is PathsActions.SetRomsPath -> setRomPath(action.uri)
            is PathsActions.SetTrianglePath -> setTrianglePath(action.uri)
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
