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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class GameRepository(
    private val doa: GamesDbDoa,
    private val config: ConfigRepository,
    private val context: Context
) : GameRepository {
    val repositoryScope = CoroutineScope(Dispatchers.IO)

    override fun readAllGames(): Flow<Game> {
        Log.i("GameRepo", "Running readAllGames()")
        val romPathFlow = config.romUriFlow
        val userPathFlow = config.triangleDataUriFlow
        return channelFlow {
            val romPath = romPathFlow.first()
            val userPath = userPathFlow.first()
            if (romPath == null || userPath == null) {
                return@channelFlow
            }
            Log.i("GameRepo", "Reading games...")
            val romList = getRomFiles(context, romPath)
            Log.i("GameRepo", "Read games")
            launch {

                romList.map { romUri ->
                    Log.i(
                        "GameRepo",
                        "Reading game: ${DocumentFile.fromSingleUri(context, romUri)?.name}"
                    )
                    async {
                        try {
                            val game = idRom(romUri, context, doa, userPath)
                            send(game)
                            Log.i("GameRepo", "Game Added: $game")
                    }
                        catch (e: Exception) {
                            Log.e("GameRepo", "Failed to process ROM : $romUri")
                        }
                    }
                }.awaitAll()
            }
            awaitClose {
                Log.i("GameRepo", "All ROMs read, Flow Closing")
            }
        }.flowOn(Dispatchers.IO)
    }


    suspend fun idRom(romPath: Uri, context: Context, doa: GamesDbDoa, userPath: Uri): Game {
        val file = DocumentFile.fromSingleUri(context, romPath) ?: throw IllegalArgumentException("File not Valid: $romPath")
        val romID = doa.getRomIdFromName(file.name?.substringBeforeLast(".")) ?: doa.getRomId(hasher(context, romPath)) ?:-1
        val romName = doa.getName(romID) ?: file.name?.substringBeforeLast('.') ?: "Unknown file"
        val coverURI =
            doa.getUSACoverURI(romID)?.toUri() ?: doa.getCoverURI(romID)?.toUri() ?: Uri.EMPTY
        val console = fileMapper(context, romPath)
        return Game(
            name = romName,
            coverDownloaderUri = coverURI,
            romID = romID,
            path = romPath,
            consoles = console,
            cover = findCover(
                context = context,
                userFolder = userPath,
                romID = romID,
            ) ?: downloadCover(
                context = context,
                gameName = romName,
                romID = romID,
                imageUri = coverURI,
                userFolder = userPath
            )
        )
    }
}
