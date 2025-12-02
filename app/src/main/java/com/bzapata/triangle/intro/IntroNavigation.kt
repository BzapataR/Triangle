package com.bzapata.triangle.intro

import kotlinx.serialization.Serializable

@Serializable
sealed interface IntroNavigation {

    @Serializable
    data object IntroNavigationGraph : IntroNavigation

    @Serializable
    data object Welcome : IntroNavigation

    @Serializable
    data object Permissions : IntroNavigation

    @Serializable
    data object Paths : IntroNavigation

    @Serializable
    data object Done : IntroNavigation
}