package com.bzapata.triangle.util.controllerIcons

import com.bzapata.triangle.R
import com.bzapata.triangle.data.controller.ControllerManager
//todo Shoutout @kenny.nl(bluesky) for these free assets

fun aIcon(controllerType : ControllerManager.ControllerType) : Int {
    return when (controllerType) {
        ControllerManager.ControllerType.XBOX, ControllerManager.ControllerType.OTHER -> R.drawable.xbox_button_color_a_outline
        ControllerManager.ControllerType.PLAYSTATION -> R.drawable.playstation_button_color_cross_outline
        ControllerManager.ControllerType.NINTENDO -> R.drawable.switch_button_b_outline
        ControllerManager.ControllerType.KEYBOARD -> R.drawable.keyboard_space_icon_outline
    }
}

fun bIcon(controllerType : ControllerManager.ControllerType) : Int {
    return when (controllerType) {
        ControllerManager.ControllerType.XBOX, ControllerManager.ControllerType.OTHER -> R.drawable.xbox_button_color_b_outline
        ControllerManager.ControllerType.PLAYSTATION -> R.drawable.playstation_button_color_circle_outline
        ControllerManager.ControllerType.NINTENDO -> R.drawable.switch_button_a_outline
        ControllerManager.ControllerType.KEYBOARD -> R.drawable.keyboard_escape_outline
    }
}

fun yIcon(controllerType : ControllerManager.ControllerType) : Int? {
    return when (controllerType) {
        ControllerManager.ControllerType.XBOX, ControllerManager.ControllerType.OTHER -> R.drawable.xbox_button_color_y_outline
        ControllerManager.ControllerType.PLAYSTATION -> R.drawable.playstation_button_color_triangle_outline
        ControllerManager.ControllerType.NINTENDO -> R.drawable.switch_button_x_outline
        ControllerManager.ControllerType.KEYBOARD -> null
    }
}

fun xIcons(controllerType : ControllerManager.ControllerType) : Int? {
    return when (controllerType) {
        ControllerManager.ControllerType.XBOX, ControllerManager.ControllerType.OTHER -> R.drawable.xbox_button_color_x_outline
        ControllerManager.ControllerType.PLAYSTATION -> R.drawable.playstation_button_color_square_outline
        ControllerManager.ControllerType.NINTENDO -> R.drawable.switch_button_y_outline
        ControllerManager.ControllerType.KEYBOARD -> null
    }
}

fun rightTriggerIcons(controllerType : ControllerManager.ControllerType) : Int {
    return when (controllerType) {
        ControllerManager.ControllerType.XBOX, ControllerManager.ControllerType.OTHER -> R.drawable.xbox_rt_outline
        ControllerManager.ControllerType.PLAYSTATION -> R.drawable.playstation_trigger_r1_alternative_outline
        ControllerManager.ControllerType.NINTENDO -> R.drawable.switch_button_r_outline
        ControllerManager.ControllerType.KEYBOARD -> R.drawable.keyboard_e_outline
    }
}

fun rightBumperIcons(controllerType : ControllerManager.ControllerType) : Int? {
    return when (controllerType) {
        ControllerManager.ControllerType.XBOX, ControllerManager.ControllerType.OTHER -> R.drawable.xbox_rb_outline
        ControllerManager.ControllerType.PLAYSTATION -> R.drawable.playstation_trigger_r2_alternative_outline
        ControllerManager.ControllerType.NINTENDO -> R.drawable.switch_button_zr_outline
        ControllerManager.ControllerType.KEYBOARD -> null
    }
}

fun leftTriggerIcons(controllerType : ControllerManager.ControllerType) : Int {
    return when (controllerType) {
        ControllerManager.ControllerType.XBOX, ControllerManager.ControllerType.OTHER -> R.drawable.xbox_lt_outline
        ControllerManager.ControllerType.PLAYSTATION -> R.drawable.playstation_trigger_l1_alternative_outline
        ControllerManager.ControllerType.NINTENDO -> R.drawable.switch_button_l_outline
        ControllerManager.ControllerType.KEYBOARD -> R.drawable.keyboard_q_outline
    }
}

fun leftBumperIcons(controllerType : ControllerManager.ControllerType) : Int? {
    return when (controllerType) {
        ControllerManager.ControllerType.XBOX, ControllerManager.ControllerType.OTHER -> R.drawable.xbox_lb_outline
        ControllerManager.ControllerType.PLAYSTATION -> R.drawable.playstation_trigger_l2_alternative_outline
        ControllerManager.ControllerType.NINTENDO -> R.drawable.switch_button_zl_outline
        ControllerManager.ControllerType.KEYBOARD -> null
    }
}

fun startIcons(controllerType : ControllerManager.ControllerType) : Int? {
    return when (controllerType) {
        ControllerManager.ControllerType.XBOX, ControllerManager.ControllerType.OTHER -> R.drawable.xbox_button_start_icon_outline
        ControllerManager.ControllerType.PLAYSTATION -> R.drawable.playstation3_button_start_outline
        ControllerManager.ControllerType.NINTENDO -> R.drawable.switch_button_plus_outline
        ControllerManager.ControllerType.KEYBOARD -> null
    }
}

fun selectIcons(controllerType : ControllerManager.ControllerType) : Int? {
    return when (controllerType) {
        ControllerManager.ControllerType.XBOX, ControllerManager.ControllerType.OTHER -> R.drawable.xbox_button_back_icon_outline
        ControllerManager.ControllerType.PLAYSTATION -> R.drawable.playstation3_button_select_outline
        ControllerManager.ControllerType.NINTENDO -> R.drawable.switch_button_minus_outline
        ControllerManager.ControllerType.KEYBOARD -> null
    }
}

