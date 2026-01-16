package com.bzapata.triangle.data.controller

import android.content.Context
import android.hardware.input.InputManager
import android.os.VibrationEffect
import android.util.Log
import android.view.InputDevice
import android.view.KeyEvent
import android.view.MotionEvent
import com.bzapata.triangle.emulatorScreen.presentation.EmulatorActions
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ControllerManager(context : Context) : InputManager.InputDeviceListener { // gamepad and key/mouse input events
    private val tag = "Controller"
    private val inputManager = context.getSystemService(Context.INPUT_SERVICE) as InputManager

    private val _connectedControllers = MutableStateFlow<List<ConnectedController>>(emptyList())
    val connectedController = _connectedControllers.asStateFlow()
    private val _recentControllerType = MutableStateFlow<ControllerType?>(null)
    val recentControllerType = _recentControllerType.asStateFlow()

    enum class ControllerType(){
        XBOX,
        PLAYSTATION,
        NINTENDO,
        KEYBOARD,
        OTHER,
    }
    data class ConnectedController(
        val id: Int,
        val name: String,
        val type : ControllerType,
        val vendorID : Int,
        val productId : Int,
    )
    init {
        inputManager.registerInputDeviceListener(this, null)
        updateControllerList()
    }
    private fun updateControllerList() {
        val controllers = inputManager.inputDeviceIds.map { id ->
            val device = inputManager.getInputDevice(id)
            if (device != null && isSupportedInput(device)) {
                Log.i(tag, "Device Added: ${device.name}")
                _recentControllerType.update { identifyController(device) }// todo update other functions to update state
                ConnectedController(
                    id = id,
                    name = device.name,
                    type = identifyController(device) ?: return@map null,
                    vendorID = device.vendorId,
                    productId = device.productId
                )
            }
            else {
                null
            }
        }.filterNotNull()
        _connectedControllers.update { controllers }
    }
//    private fun detectGamepadBrand(device: InputDevice): InputType { below works fine for now
//        return when (device.vendorId) {
//            0x054C -> InputType.PLAYSTATION   // Sony
//            0x045E -> InputType.XBOX          // Microsoft
//            0x057E -> InputType.NINTENDO      // Nintendo
//            else -> InputType.UNKNOWN
//        }
//    }

    private fun identifyController(device : InputDevice) : ControllerType? {
        val name = device.name.lowercase()
        if (isSupportedInput(device)) {
            return when {
                name.contains("xbox") -> ControllerType.XBOX
                name.contains("dualshock") || name.contains("dualsense") || name.contains("playstation") -> ControllerType.PLAYSTATION
                name.contains("joy-con") || name.contains("pro controller") || name.contains("nintendo") -> ControllerType.NINTENDO
                (device.sources and InputDevice.SOURCE_KEYBOARD) == InputDevice.SOURCE_KEYBOARD &&
                        device.keyboardType == InputDevice.KEYBOARD_TYPE_ALPHABETIC -> ControllerType.KEYBOARD

                else -> ControllerType.OTHER
            }
        }
        return null // touch input
    }
    fun vibrate(deviceId: Int, durationMillis : Long = 100) {
        val device = inputManager.getInputDevice(deviceId) ?: return
        val vibrator = device.vibratorManager.defaultVibrator
        vibrator.vibrate(
            VibrationEffect.createOneShot(
                durationMillis,
                VibrationEffect.DEFAULT_AMPLITUDE
            )
        )
    }

    override fun onInputDeviceAdded(deviceId: Int) {
        Log.i(tag, "Input Added")
        updateControllerList()
    }

    override fun onInputDeviceChanged(deviceId: Int) {
        Log.i(tag, "Input Changed")
        updateControllerList()
    }

    override fun onInputDeviceRemoved(deviceId: Int) {
        Log.i(tag, "Input Removed")
        updateControllerList()
    }
    fun inputDeviceDetection(event : KeyEvent) : ControllerType?  {
        val type = identifyController(event.device)
        Log.i(tag, "deviceBrand : $type")
        return type
    }
//    fun inputDeviceDetection(event : MotionEvent) : ControllerType?  {
//        val type = identifyController(event.device)
//        Log.i(tag, "deviceBrand : $type")
//        return type
//    }
    fun buttonActionMapping(event: KeyEvent): EmulatorActions? {
        val keyCode = event.keyCode
        if (event.repeatCount != 0) return null
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
    fun motionActionMapping(event: MotionEvent) : EmulatorActions? {
        if (!isSupportedInput(event.device)) {
            return null
        }
        val lTrigger = maxOf(
            event.getAxisValue(MotionEvent.AXIS_LTRIGGER),
            event.getAxisValue(MotionEvent.AXIS_BRAKE),
            event.getAxisValue(MotionEvent.AXIS_Z)
        )
        val rTrigger = maxOf(
            event.getAxisValue(MotionEvent.AXIS_RTRIGGER),
            event.getAxisValue(MotionEvent.AXIS_GAS),
            event.getAxisValue(MotionEvent.AXIS_RZ)
        )
        val triggerThreshold = 0.25f
        val ltPressed = lTrigger > triggerThreshold
        val rtPressed = rTrigger > triggerThreshold

        return when {
            rtPressed -> EmulatorActions.MovePage(Int.MAX_VALUE)
            ltPressed -> EmulatorActions.MovePage(Int.MIN_VALUE)
            else -> null
        }
    }
    private fun isSupportedInput(device: InputDevice): Boolean {
        val sources = device.sources
        if (device.isVirtual) {
            return false
        }
        return (sources and InputDevice.SOURCE_GAMEPAD == InputDevice.SOURCE_GAMEPAD) ||
                (sources and InputDevice.SOURCE_JOYSTICK == InputDevice.SOURCE_JOYSTICK) ||
                ((sources and InputDevice.SOURCE_KEYBOARD == InputDevice.SOURCE_KEYBOARD) && device.keyboardType == InputDevice.KEYBOARD_TYPE_ALPHABETIC)
    }
}