package com.bzapata.triangle.deltaCoreKt.model.inputs

import com.bzapata.triangle.deltaCoreKt.Delta
import com.bzapata.triangle.deltaCoreKt.protocols.inputs.Input
import com.bzapata.triangle.deltaCoreKt.protocols.inputs.InputType
import com.bzapata.triangle.deltaCoreKt.protocols.model.GameControllerInputMappingProtocol
import com.bzapata.triangle.deltaCoreKt.types.GameControllerInputType
import com.bzapata.triangle.deltaCoreKt.types.GameType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.koin.java.KoinJavaComponent.get

@Serializable
enum class StandardGameControllerInput(val rawValue: String) : Input {
    @SerialName("menu") MENU("menu"),
    @SerialName("up") UP("up"),
    @SerialName("down") DOWN("down"),
    @SerialName("left") LEFT("left"),
    @SerialName("right") RIGHT("right"),
    @SerialName("leftThumbstickUp") LEFT_THUMBSTICK_UP("leftThumbstickUp"),
    @SerialName("leftThumbstickDown") LEFT_THUMBSTICK_DOWN("leftThumbstickDown"),
    @SerialName("leftThumbstickLeft") LEFT_THUMBSTICK_LEFT("leftThumbstickLeft"),
    @SerialName("leftThumbstickRight") LEFT_THUMBSTICK_RIGHT("leftThumbstickRight"),
    @SerialName("rightThumbstickUp") RIGHT_THUMBSTICK_UP("rightThumbstickUp"),
    @SerialName("rightThumbstickDown") RIGHT_THUMBSTICK_DOWN("rightThumbstickDown"),
    @SerialName("rightThumbstickLeft") RIGHT_THUMBSTICK_LEFT("rightThumbstickLeft"),
    @SerialName("rightThumbstickRight") RIGHT_THUMBSTICK_RIGHT("rightThumbstickRight"),
    @SerialName("a") A("a"),
    @SerialName("b") B("b"),
    @SerialName("x") X("x"),
    @SerialName("y") Y("y"),
    @SerialName("start") START("start"),
    @SerialName("select") SELECT("select"),
    @SerialName("l1") L1("l1"),
    @SerialName("l2") L2("l2"),
    @SerialName("l3") L3("l3"),
    @SerialName("r1") R1("r1"),
    @SerialName("r2") R2("r2"),
    @SerialName("r3") R3("r3");
    override val stringValue: String get() = rawValue
    override val type : InputType get() = InputType.Controller(GameControllerInputType.standard)
    override val isContinuous : Boolean get() = when(this) {
        LEFT_THUMBSTICK_UP, LEFT_THUMBSTICK_DOWN, LEFT_THUMBSTICK_LEFT, LEFT_THUMBSTICK_RIGHT, RIGHT_THUMBSTICK_UP, RIGHT_THUMBSTICK_DOWN, RIGHT_THUMBSTICK_LEFT, RIGHT_THUMBSTICK_RIGHT -> true
        else -> false
    }

    fun input(gameType : GameType) : Input? {
        val mapping = inputMapping[gameType]
        if(mapping != null){
            return mapping.input(this)
        }
        val delta : Delta = get(Delta::class.java)
        try {
            val deltaCore = delta.core(gameType) ?: return null
            val fileUri = //TODO: load standard controls for core
        }
        catch (e : Exception) {
            throw Exception("can't find standard.deltamapping for game type $gameType")
        }
    }
    companion object {
        private val inputMapping = mutableMapOf<GameType, GameControllerInputMappingProtocol>()

        fun fromString(stringValue: String): StandardGameControllerInput? {
            return entries.find { it.rawValue == stringValue }
        }

        operator fun invoke(input : Input) : StandardGameControllerInput? {
            if (input.type != InputType.Controller(GameControllerInputType.standard)) return null
                return fromString(input.stringValue)
        }
    }
}
