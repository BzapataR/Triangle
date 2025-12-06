package com.bzapata.triangle.intro

data class IntroState(
    val page : Int = 0,
    val hasNotificationPermission : Boolean = false,
    val hasMicPermission : Boolean = false,
    val hasCameraPermission : Boolean = false,
)