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
import com.bzapata.triangle.emulatorScreen.data.romsDatabase.SavedRomsDoa
import com.bzapata.triangle.emulatorScreen.data.romsDatabase.toGame
import com.bzapata.triangle.emulatorScreen.data.romsDatabase.toSavedRomsEntity
import com.bzapata.triangle.emulatorScreen.domain.Game
import com.bzapata.triangle.emulatorScreen.domain.GameRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.withContext

class GameRepository(
    private val gamesDoa: GamesDbDoa,
    private val config: ConfigRepository,
    private val context: Context,
    private val savedRomsDoa: SavedRomsDoa,
) : GameRepository {
    private val scope = CoroutineScope(Dispatchers.IO)

    override suspend fun scanRoms() = withContext(Dispatchers.IO) {
        val romPath = config.romUriFlow.first()
        val userPath = config.triangleDataUriFlow.first()

        if (romPath == null || userPath == null) {
            Log.w("GameRepo", "ROM or User path not configured, aborting scan.")
            return@withContext
        }

        Log.i("GameRepo", "Starting ROM scan for path: $romPath")
        val romUris = getRomFiles(context, romPath).toList()

        romUris.map { romUri ->
            async {
                idRom(romUri, context, gamesDoa, userPath)
            }
        }.awaitAll()

        Log.i("GameRepo", "ROM scan finished. Validating database...")
        validateSavedRoms()
    }

    override fun getGames(): Flow<List<Game>> {
        return savedRomsDoa.getAllSavedRoms()?.map { it.toGame() } ?: return emptyFlow()
    }

    override fun databaseBomb() {
        savedRomsDoa.deleteAll()
    }

    private suspend fun validateSavedRoms() {
        Log.i("GameRepo", "Starting background validation of saved games.")
        val dbGames = savedRomsDoa.getAllSavedRoms()?.first() ?: return

        val gamesToDelete = dbGames.filter { savedRom ->
            val file = DocumentFile.fromSingleUri(context, savedRom.path.toUri())
            file == null || !file.exists()
        }

        if (gamesToDelete.isNotEmpty()) {
            Log.i("GameRepo", "Found ${gamesToDelete.size} deleted ROMs. Removing from database.")
            gamesToDelete.forEach { savedRomsDoa.deleteRomsFromDb(it) }
        }
        Log.i("GameRepo", "Validation finished.")
    }

    private suspend fun idRom(
        romPath: Uri,
        context: Context,
        doa: GamesDbDoa,
        userPath: Uri
    ): Game {
        val file = DocumentFile.fromSingleUri(context, romPath)
            ?: throw IllegalArgumentException("File not Valid: $romPath")
        val hash = hasher(context, romPath)
        val romID = doa.getRomIdFromName(file.name?.substringBeforeLast('.'))
            ?: doa.getRomId(hash) ?: -1
        val romName = doa.getName(romID) ?: file.name?.substringBeforeLast('.') ?: "Unknown file"
        val coverURI = doa.getCoverURI(romID)?.map { it.toUri() } ?: emptyList()
        val console = fileMapper(context, romPath)

        val rom = Game(
            name = romName,
            coverDownloaderUri = coverURI,
            romID = romID,
            path = romPath,
            consoles = console,
            hash = hash,
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
        val lastModified = file.lastModified()
        savedRomsDoa.upsert(rom.toSavedRomsEntity(lastModified))
        return rom
    }

    suspend fun queryCovers(titleName : String) : Map<Uri,String?> = withContext(Dispatchers.IO) {
        if (titleName.isBlank()) return@withContext emptyMap()
        gamesDoa.queryCover(titleName.trim()).associate { it.releaseCoverFront.toUri() to it.releaseTitleName }
    }

}
