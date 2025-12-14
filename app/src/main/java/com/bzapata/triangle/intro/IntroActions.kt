package com.bzapata.triangle.intro

sealed interface IntroActions {
    data object GoBack : IntroActions
    data object Finish : IntroActions
    data class ChangePage(val page: Int) : IntroActions
    data class PermissionStatusChange(val permission: String, val isGranted: Boolean) : IntroActions
}
