package com.bzapata.triangle.deltaCoreKt.protocols.model

import com.bzapata.triangle.deltaCoreKt.protocols.inputs.Input
import com.bzapata.triangle.deltaCoreKt.types.GameControllerInputType

interface GameControllerInputMappingProtocol {
    var gameControllerInputType : GameControllerInputType

    fun input(controllerInput : Input) : Input?
}