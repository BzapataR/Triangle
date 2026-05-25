package com.bzapata.triangle.deltaCoreKt.model

import com.bzapata.triangle.deltaCoreKt.protocols.inputs.GameController
import com.bzapata.triangle.deltaCoreKt.protocols.inputs.GameControllerReceiver
import com.bzapata.triangle.deltaCoreKt.protocols.inputs.Input
import com.bzapata.triangle.deltaCoreKt.protocols.inputs.InputType
import com.bzapata.triangle.deltaCoreKt.protocols.model.GameControllerInputMappingProtocol
import java.util.WeakHashMap

internal class GameControllerStateManager(val gameController: GameController) {
    // note to whoever reads this. I saw swift running on android tried it and decided to rewrite it
    // to kotlin due to limitations, such as async code troubles between languages.
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
                receiver.gameController(gameController, mappedInput, value)
            }
        }
    }

    fun deactivate(input : Input) {
        require(input.type is InputType.Controller && input.type.inputType == gameController.inputType) {
            "input.type must match gameController.inputType"
        }
        if(!_activatedInputs.containsKey(input)) return

        val sustainedValue = _sustainedInputs[input]
        if (sustainedValue != null) {
            if (input.isContinuous) {
                // Reset to sustained value if continuous
                activate(input, sustainedValue)
            }
        }
        else {
            // Not sustained, perform full deactivation
            _activatedInputs.remove(input)

            for(receiver in receivers) {
                val mappedInput = mappedInput(input,receiver) ?: continue

                // Check if any other physical inputs still map to this same virtual inputs
                val stillHasActiveMappings = _activatedInputs.keys.any { activeInputs ->
                    mappedInput(activeInputs, receiver) == mappedInput
                }

                if (!stillHasActiveMappings) {
                    receiver.gameController(gameController, mappedInput)
                }
            }
        }
    }

    fun sustain(input: Input, value: Double) {
        require(input.type is InputType.Controller && input.type.inputType == gameController.inputType) {
            "input.type must match gameController.inputType"
        }
        if (_activatedInputs[input] != value) {
            activate(input, value)
        }

        _sustainedInputs[input] = value
    }

    fun unsustain(input: Input) {
        require(input.type is InputType.Controller && input.type.inputType == gameController.inputType) {
            "input.type must match gameController.inputType"
        }
        _sustainedInputs.remove(input)
        deactivate(input)
    }


    fun inputMapping(receiver: GameControllerReceiver) : GameControllerInputMappingProtocol? {
        return synchronized(_receivers) {
            _receivers[receiver]
        }
    }

    fun mappedInput(input : Input, receiver : GameControllerReceiver) : Input? {
        val mapping = inputMapping(receiver) ?: return input
        return mapping.input(input)
    }
}