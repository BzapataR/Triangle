package com.bzapata.triangle.emulatorScreen.domain



enum class Consoles(
    val fullName: String,
    val brand: String,
    val extensions: List<String>
) {
    NES(
        fullName = "Nintendo Entertainment System",
        brand = "Nintendo",
        extensions = listOf("nes")
    ),
    SNES(
        fullName = "Super Nintendo",
        brand = "Nintendo",
        extensions = listOf("sfc", "smc", "fig")
    ),
    N64(
        fullName = "Nintendo 64",
        brand = "Nintendo",
        extensions = listOf("n64", "z64", "v64")
    ),
    GB(
        fullName = "Game Boy",
        brand = "Nintendo",
        extensions = listOf("gb", "gbc")
    ),
    GBA(
        fullName = "Game Boy Advance",
        brand = "Nintendo",
        extensions = listOf("gba")
    ),
    DS(
        fullName = "Nintendo DS",
        brand = "Nintendo",
        extensions = listOf("nds", ".ds")
    );

    companion object {
        fun fromExtension(ext: String): Consoles? {
            val normalizedExt = ext.lowercase().removePrefix(".")
            return entries.find { it.extensions.contains(normalizedExt) }
        }
    }
}
