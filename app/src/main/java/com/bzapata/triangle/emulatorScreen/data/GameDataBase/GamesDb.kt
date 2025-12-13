package com.bzapata.triangle.emulatorScreen.data.GameDataBase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
// 11/26/25
@Database(entities = [ROMsEntity::class, ReleaseEntity::class], version = 1)
abstract class GamesDb: RoomDatabase () {
    //private static final DATABASE_NAME = ""
    abstract fun gameDbDoa(): GamesDbDoa

    companion object {
        @Volatile
        private var INSTANCE: GamesDb? = null

        fun getDataBase(context: Context): GamesDb {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    GamesDb::class.java,
                    "openvgdb"
                )
                    .createFromAsset("openvgdb.sqlite")
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

}