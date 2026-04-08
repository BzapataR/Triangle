package com.bzapata.triangle.deltaCoreKt.model.inputs

import com.bzapata.triangle.deltaCoreKt.protocols.inputs.GameController
import com.bzapata.triangle.deltaCoreKt.protocols.inputs.GameControllerReceiver
import com.bzapata.triangle.deltaCoreKt.protocols.inputs.Input
import com.bzapata.triangle.deltaCoreKt.protocols.inputs.InputType
import com.bzapata.triangle.deltaCoreKt.protocols.model.GameControllerInputMappingProtocol
import java.util.WeakHashMap

internal class GameControllerStateManager(val gameController: GameController) {
    private var _activatedInputs = mutableMapOf<Input, Double>()
    val activatedInputs: Map<Input, Double> get() = _activatedInputs

    private val _sustainedInputs = mutableMapOf<Input, Double>()
    val sustainedInputs: Map<Input, Double> get() = _sustainedInputs

    private val _receivers =
        WeakHashMap<GameControllerReceiver, GameControllerInputMappingProtocol?>()
    val receivers : List<GameControllerReceiver>
        get() = synchronized(_receivers) {
            _receivers.keys.toList()
        }

    fun addReceiver(receiver : GameControllerReceiver, inputMapping : GameControllerInputMappingProtocol?) {
        synchronized(_receivers) {
            _receivers[receiver] = inputMapping
        }
    }
    fun removeReceiver(receiver : GameControllerReceiver) {
        synchronized(_receivers) {
            _receivers.remove(receiver)
        }
    }

    fun activate(input : Input, value : Double) {
        //validation ensures the input belongs to this controller
        require(input.type is InputType.Controller && input.type.inputType == gameController.inputType) {
            "input.type must match gameController.inputType"
        }
        _activatedInputs[input] = value

        for (receiver in receivers) {
            val mappedInput = mappedInput(input, receiver)
            if (mappedInput != null) {
                receiver.onGameControllerActivated(gameController, mappingInput, value)
            }
        }
    }
}

