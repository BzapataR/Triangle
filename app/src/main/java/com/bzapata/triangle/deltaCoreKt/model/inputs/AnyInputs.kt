package com.bzapata.triangle.deltaCoreKt.model.inputs

import com.bzapata.triangle.deltaCoreKt.Delta
import com.bzapata.triangle.deltaCoreKt.protocols.inputs.Input
import com.bzapata.triangle.deltaCoreKt.protocols.inputs.InputType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.koin.java.KoinJavaComponent.get

@Serializable
data class AnyInput (
    @SerialName("identifier") override val stringValue: String,
    override val type : InputType,
    override val intValue : Int? = null,
    override val isContinuous : Boolean = false
) : Input {
    constructor(input : Input) : this(
        stringValue = input.stringValue,
        intValue = input.intValue,
        type = input.type,
        isContinuous = input.isContinuous
    )
    companion object {
        fun create (
            stringValue: String,
            type : InputType,
            intValue: Int? = null,
            isContinuous : Boolean? = null,
        ) : AnyInput {
            var resolvedIsContinuous = isContinuous ?: false
            var resolvedIntValue = intValue

            if (isContinuous == null) {
                when (type) {
                    is InputType.Game -> {
                        val delta : Delta = get(Delta::class.java)
                        val core = delta.core(type.type)
                        //TODO: get core-specific input look up to resolve is Continuous
                        // e.g. val input = core.gameInputType.from(stringvalue)
                    }
                    is InputType.Controller -> {
                        val standardInput = StandardGameController //TODO: finish implementing this
                    }
                }
            }
            return AnyInput(stringValue, type, resolvedIntValue, resolvedIsContinuous)
        }
    }
}