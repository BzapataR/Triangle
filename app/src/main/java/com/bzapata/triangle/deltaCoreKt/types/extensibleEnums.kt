package com.bzapata.triangle.deltaCoreKt.types

import kotlinx.serialization.Serializable

@Serializable
@JvmInline
value class GameType(val rawValue : String) {
    override fun toString(): String = rawValue
    companion object {
        val unknown = GameType("com.rielytestut.delta.game.unknown")
    }
}

@Serializable
@JvmInline
value class CheatType(val rawValue: String)

@Serializable
@JvmInline
value class GameControllerInputType(val rawValue: String)