package com.bzapata.triangle.emulatorScreen.data.romsDatabase

import android.net.Uri
import androidx.core.net.toUri
import androidx.room.TypeConverter
import com.bzapata.triangle.emulatorScreen.domain.Consoles

class SavedRomsTypeConverter {
    // These converters are correct
    @TypeConverter
    fun fromUri(uri: Uri?): String? {
        return uri?.toString()
    }

    @TypeConverter
    fun toUri(uriString: String?): Uri? {
        return uriString?.toUri()
    }

    @TypeConverter
    fun fromConsoles(console: Consoles): String {
        return console.name
    }

    @TypeConverter
    fun toConsole(consoleName: String): Consoles {
        return Consoles.valueOf(consoleName)
    }

    private val separator = "|||" // A separator unlikely to be in a real URI

    @TypeConverter
    fun fromStringList(stringList: List<String?>): String {
        return stringList.joinToString(separator) { it ?: "null" }
    }

    @TypeConverter
    fun toStringList(data: String): List<String?> {
        if (data.isEmpty()) return emptyList()
        return data.split(separator).map { if (it == "null") null else it }
    }
}