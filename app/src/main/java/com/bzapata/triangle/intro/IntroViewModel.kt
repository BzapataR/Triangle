package com.bzapata.triangle.intro

import android.Manifest
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bzapata.triangle.data.repository.ConfigRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class IntroViewModel(private val configRepository: ConfigRepository) : ViewModel() {
    private var observerConfig: Job? = null
    private var _state = MutableStateFlow(IntroState())
    var state = _state.asStateFlow()
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
        observerConfig = configRepository.triangleDataUriFlow.onEach { trianglePath ->
            _state.update {
                it.copy(
                    triangleFolderEmpty = trianglePath == null,
                )
            }
        }.launchIn(viewModelScope)
    }


    fun onAction(actions: IntroActions) {
        when (actions) {
            is IntroActions.GoBack -> decrementPage()
            is IntroActions.Finish -> finishIntro()
            is IntroActions.ChangePage -> changePage(actions.page)
            is IntroActions.PermissionStatusChange -> updatePermissionStatus(
                actions.permission,
                actions.isGranted
            )
        }
    }

    private fun updatePermissionStatus(permission: String, isGranted: Boolean) {
        Log.i("Permission", "permission: $permission, isGranted: $isGranted")
        _state.update {
            when (permission) {
                Manifest.permission.POST_NOTIFICATIONS -> it.copy(
                    hasNotificationPermission = isGranted,
                )

                Manifest.permission.RECORD_AUDIO -> it.copy(hasMicPermission = isGranted)
                Manifest.permission.CAMERA -> it.copy(hasCameraPermission = isGranted)
                else -> {
                    Log.e("Permission", "Wrong Permission")
                    it
                }
            }
        }
        _state.update {
            it.copy(
                showSkipPermissionDialog = it.hasNotificationPermission ||
                        it.hasMicPermission ||
                        it.hasCameraPermission
            )
        }
        Log.i("Permission", "new view model state ${_state.value}")
    }

    private fun changePage(page: Int) {
        _state.update {
            it.copy(
                page = page
            )
        }
    }

    private fun decrementPage() {
        _state.update {
            it.copy(page = it.page - 1)
        }
    }

    private fun finishIntro() {
        viewModelScope.launch {
            configRepository.changeFirstLaunch()
        }
    }
}
