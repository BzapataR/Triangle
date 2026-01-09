package com.bzapata.triangle.data.controller

import android.view.InputDevice
import android.view.KeyEvent
import android.view.MotionEvent
import com.bzapata.triangle.emulatorScreen.presentation.EmulatorActions

class Controller { // gamepad and key/mouse input events

    //    sealed interface KeyCodeEvents {
//        data object ToggleSettings : KeyCodeEvents
//        data object ToggleContextMenu : KeyCodeEvents
//        data object ToggleSearch : KeyCodeEvents
//        data object RightOnePage : KeyCodeEvents
//        data object LeftOnePage : KeyCodeEvents
//        data object ToLastPage : KeyCodeEvents
//        data object ToFirstPage : KeyCodeEvents
//        data object Refresh : KeyCodeEvents
//    }
    enum class ControllerType(){
        XBOX,
        PLAYSTATION,
        NINTENDO_SWITCH,
        KEYBOARD,
        OTHER,
    }
    fun onKeyDown(event: KeyEvent): EmulatorActions? {
        val keyCode = event.keyCode
        if (!isSupportedInput(event) || event.repeatCount != 0) return null
        if (event.action != KeyEvent.ACTION_DOWN) return null
        return when (keyCode) {
            // GLOBAL UI NAVIGATION
            KeyEvent.KEYCODE_BUTTON_SELECT -> {
                EmulatorActions.ToggleSettings
            }

            KeyEvent.KEYCODE_F1, KeyEvent.KEYCODE_BUTTON_START -> {
                EmulatorActions.ToggleFileContextMenu
            }

            KeyEvent.KEYCODE_BUTTON_Y -> { /*toggle search */ null
            }

            KeyEvent.KEYCODE_BUTTON_R1, KeyEvent.KEYCODE_E -> {
                EmulatorActions.MovePage(1)
            }

            KeyEvent.KEYCODE_BUTTON_L1, KeyEvent.KEYCODE_Q -> {
                EmulatorActions.MovePage(-1)
            }

            KeyEvent.KEYCODE_BUTTON_R2 -> {
                EmulatorActions.MovePage(Int.MAX_VALUE)
            }

            KeyEvent.KEYCODE_BUTTON_L2 -> {
                EmulatorActions.MovePage(Int.MIN_VALUE)
            }

            KeyEvent.KEYCODE_F5 -> {
                EmulatorActions.RefreshRomList
            }

            else -> null
        }
    }

    fun onGenericMotionEvent(event: MotionEvent): Boolean {
        //TODO Handle Scroll Wheel or Right Stick scrolling
        return false
    }

    fun isSupportedInput(event: KeyEvent): Boolean {
        val sources = event.device?.sources ?: 0
        return (sources and InputDevice.SOURCE_GAMEPAD == InputDevice.SOURCE_GAMEPAD) ||
                (sources and InputDevice.SOURCE_KEYBOARD == InputDevice.SOURCE_KEYBOARD) ||
                (sources and InputDevice.SOURCE_DPAD == InputDevice.SOURCE_DPAD)
    }
}