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
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.scan

class GameRepository(
    private val doa: GamesDbDoa,
    private val config: ConfigRepository,
    private val context: Context
) : GameRepository {

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun readAllGames(): Flow<List<Game>> {
        return combine(config.romUriFlow, config.triangleDataUriFlow) { romPath, userPath ->
            romPath to userPath
        }.flatMapLatest { (romPath, userPath) ->
            if (romPath == null || userPath == null) {
                Log.w("GameRepo", "ROM or User path not configured, aborting scan.")
                flowOf(emptyList())
            } else {
                Log.i("GameRepo", "Starting ROM scan for path: $romPath")
                getRomFiles(context, romPath)
                    .scan(emptyList<Game>()) { accumulatedGames, newRomUri ->
                        val game = idRom(newRomUri, context, doa, userPath)
                        (accumulatedGames + game).sortedBy { it.name }
                    }
            }
        }.flowOn(Dispatchers.IO)
    }


    private suspend fun idRom(
        romPath: Uri,
        context: Context,
        doa: GamesDbDoa,
        userPath: Uri
    ): Game {
        val file = DocumentFile.fromSingleUri(context, romPath)
            ?: throw IllegalArgumentException("File not Valid: $romPath")
        val romID = doa.getRomIdFromName(file.name?.substringBeforeLast('.'))
            ?: doa.getRomId(hasher(context, romPath)) ?: -1
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
