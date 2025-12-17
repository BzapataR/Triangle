package com.bzapata.triangle.emulatorScreen.data.romsDatabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [SavedRomsEntity::class], version = 1)
@TypeConverters(SavedRomsTypeConverter::class)
abstract class SavedRomsDb : RoomDatabase() {
    abstract fun romsDbDoa(): SavedRomsDoa

    companion object {
        @Volatile
        private var INSTANCE: SavedRomsDb? = null

        fun getRomsDatabase(context: Context): SavedRomsDb {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SavedRomsDb::class.java,
                    "SavedRomsDb"
                )
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}