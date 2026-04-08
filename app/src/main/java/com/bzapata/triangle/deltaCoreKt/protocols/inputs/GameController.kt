package com.bzapata.triangle.deltaCoreKt.protocols.inputs

import com.bzapata.triangle.deltaCoreKt.protocols.model.GameControllerInputMappingProtocol
import com.bzapata.triangle.deltaCoreKt.types.GameControllerInputType

private var gameControllerStateManagerKey = 0

interface GameControllerReceiver {
    // Equivalent to pressing a button, or moving an analog stick
    fun gameController(gameController : GameController, didActivate : Input, value : Double)

    // Equivalent to releasing a button or an analog stick
    fun gameController(gameController :  GameController, didDeactivate : Input)
}

interface GameController {
    var name : String
    var playerIndex : Int?
    var inputType : GameControllerInputType
    var defaultInputMapping : GameControllerInputMappingProtocol?

    private var stateManager : GameControllerStateManager {
        var stateManager =
    }
}