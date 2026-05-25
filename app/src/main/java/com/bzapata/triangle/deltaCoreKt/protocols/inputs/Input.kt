package com.bzapata.triangle.deltaCoreKt.protocols.inputs

import com.bzapata.triangle.deltaCoreKt.Delta
import com.bzapata.triangle.deltaCoreKt.types.GameControllerInputType
import com.bzapata.triangle.deltaCoreKt.types.GameType
import kotlinx.serialization.Serializable
import org.koin.java.KoinJavaComponent.get
@Serializable
sealed class InputType {
    abstract val rawValue : String
    @Serializable
    data class Controller(val inputType: GameControllerInputType) : InputType() {
        override val rawValue: String = inputType.rawValue
    }
    @Serializable
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

 interface Input {
    val stringValue : String
    val intValue : Int? get() = null
    val type: InputType

    val isContinuous: Boolean get() = false

     fun matches(other:Input): Boolean {
         return this.type == other.type && this.stringValue == other.stringValue
     }
}

interface StringRawInput : Input {
    val rawValue : String
    override val stringValue: String
        get() = rawValue
}