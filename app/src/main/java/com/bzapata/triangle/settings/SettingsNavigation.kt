package com.bzapata.triangle.settings

import kotlinx.serialization.Serializable

@Serializable
sealed interface SettingsNavigation {

    @Serializable
    data object SettingsNavigationGraph: SettingsNavigation

    @Serializable
    data object SettingsMainPage : SettingsNavigation

    @Serializable
    data object ControllerSettings : SettingsNavigation

    @Serializable
    data object ControllerSkins : SettingsNavigation

    @Serializable
    data object CloudSync : SettingsNavigation

    @Serializable
    data object HomeScreenShortcuts : SettingsNavigation

    @Serializable
    data object CoreSettings : SettingsNavigation

    @Serializable
    data object  ExportErrorLogs : SettingsNavigation

    @Serializable
    data object Licenses : SettingsNavigation

    @Serializable
    data object PrivacyPolicy : SettingsNavigation
}