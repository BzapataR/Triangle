package com.bzapata.triangle.emulatorScreen.data

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.core.net.toUri
import androidx.documentfile.provider.DocumentFile
import com.bzapata.triangle.data.repository.ConfigRepository
import com.bzapata.triangle.emulatorScreen.data.GameDataBase.GamesDbDoa
import com.bzapata.triangle.emulatorScreen.data.fileOperations.downloadCover
import com.bzapata.triangle.emulatorScreen.data.fileOperations.fileMapper
import com.bzapata.triangle.emulatorScreen.data.fileOperations.findCover
import com.bzapata.triangle.emulatorScreen.data.fileOperations.getRomFiles
import com.bzapata.triangle.emulatorScreen.data.fileOperations.hasher
import com.bzapata.triangle.emulatorScreen.domain.Game
import com.bzapata.triangle.emulatorScreen.domain.GameRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class GameRepository(
    private val doa: GamesDbDoa,
    private val config: ConfigRepository,
    private val context: Context
) : GameRepository {

    override fun readAllGames(): Flow<Game> = channelFlow {
        val romPath = config.romUriFlow.first()
        val userPath = config.triangleDataUriFlow.first()

        if (romPath == null || userPath == null) {
            Log.w("GameRepo", "ROM or User path not configured, aborting scan.")
            return@channelFlow
        }

        Log.i("GameRepo", "Starting ROM scan...")
        getRomFiles(context, romPath).collect { romUri ->
            // Launch a new coroutine for each ROM to process them in parallel
            launch {
                try {
                    val game = idRom(romUri, context, doa, userPath)
                    send(game)
                } catch (e: Exception) {
                    Log.e("GameRepo", "Failed to process ROM: $romUri", e)
                }
            }
        }

        awaitClose { Log.i("GameRepo", "ROM Flow has been closed.") }
    }.flowOn(Dispatchers.IO) // Ensure the entire flow runs on the IO dispatcher


    suspend fun idRom(romPath: Uri, context: Context, doa: GamesDbDoa, userPath: Uri): Game {
        val file = DocumentFile.fromSingleUri(context, romPath) ?: throw IllegalArgumentException("File not Valid: $romPath")
        val romID = doa.getRomIdFromName(file.name?.substringBeforeLast('.')) ?: doa.getRomId(hasher(context, romPath)) ?: -1
        val romName = doa.getName(romID) ?: file.name?.substringBeforeLast('.') ?: "Unknown file"
        val coverURI = doa.getCoverURI(romID)?.map { it.toUri() } ?: emptyList()
        val console = fileMapper(context, romPath)

        return Game(
            name = romName,
            coverDownloaderUri = coverURI,
            romID = romID,
            path = romPath,
            consoles = console,
            localCoverUri = findCover(
                context = context,
                userFolder = userPath,
                romID = romID,
            ) ?: downloadCover(
                context = context,
                gameName = romName,
                romID = romID,
                imageUris = coverURI,
                userFolder = userPath
            ) ?: Uri.EMPTY
        )
    }
}
