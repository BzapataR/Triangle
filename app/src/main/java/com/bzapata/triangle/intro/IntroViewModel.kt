package com.bzapata.triangle.intro

import android.Manifest
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bzapata.triangle.data.repository.ConfigRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class IntroViewModel(private val configRepository: ConfigRepository) : ViewModel() {
    private var _state = MutableStateFlow(IntroState())
    var state = _state.asStateFlow()

    fun onAction(actions: IntroActions) {
        when(actions){
            is IntroActions.GoBack -> decrementPage()
            is IntroActions.Finish -> finishIntro()
            is IntroActions.ChangePage -> changePage(actions.page)
            is IntroActions.PermissionStatusChange -> updatePermissionStatus(actions.permission, actions.isGranted)
            is IntroActions.SkipPage ->skipPage()
            is IntroActions.TurnOffSkip -> turnOffSkip()
        }
    }

    private fun updatePermissionStatus(permission : String, isGranted :Boolean) {
        _state.update {
            when(permission) {
                Manifest.permission.POST_NOTIFICATIONS -> it.copy(hasNotificationPermission = isGranted)
                Manifest.permission.RECORD_AUDIO -> it.copy(hasMicPermission = isGranted)
                Manifest.permission.CAMERA -> it.copy(hasCameraPermission = isGranted)
                else -> it
            }
        }
    }

    private fun changePage(page : Int) {
        _state.update {
            it.copy(
                page = page
            )
        }
    }

    private fun decrementPage() {
        _state.update {
            it.copy(page = it.page -1)
        }
    }

    private fun finishIntro() {
        viewModelScope.launch {
            configRepository.changeFirstLaunch()
        }
    }
    private fun skipPage() {
        _state.update { it.copy(showSkipDialog = true) }
    }
    private fun turnOffSkip() {
        _state.update { it.copy(showSkipDialog = false) }
    }
}
