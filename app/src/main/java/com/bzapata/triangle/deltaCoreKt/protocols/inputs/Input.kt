package com.bzapata.triangle.deltaCoreKt.protocols.inputs

import com.bzapata.triangle.deltaCoreKt.Delta
import com.bzapata.triangle.deltaCoreKt.types.GameControllerInputType
import com.bzapata.triangle.deltaCoreKt.types.GameType
import org.koin.java.KoinJavaComponent.get

sealed class InputType {
    abstract val rawValue : String
    data class Controller(val inputType: GameControllerInputType) : InputType() {
        override val rawValue: String = inputType.rawValue
    }
    data class Game(val type: GameType) : InputType() {
        override val rawValue: String = type.rawValue
    }

    companion object {
        fun fromRawValue(rawValue : String) : InputType {
            val gameType = GameType(rawValue)
            val delta : Delta = get(Delta::class.java)
            return if (delta.core(gameType) != null) {
                Game(gameType)
            } else {
                Controller(GameControllerInputType(rawValue))
            }
        }
    }
}

sealed interface Input {
    val stringValue : String
    val intValue : Int? get() = null
    val type: InputType

    val isContinuous: Boolean get() = false

}

interface StringRawInput : Input {
    val rawValue : String
    override val stringValue: String
        get() = rawValue
}