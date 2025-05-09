package com.github.markssova.gamescatalog.dao

import android.content.Context
import androidx.room.Database
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import kotlin.concurrent.Volatile

@Database(
    entities = [GameEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun gameDao(): GameDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        @Volatile
        private var DATABASE_WRITE_EXECUTOR: Executor? = null


        fun getInstance(context: Context): AppDatabase {
            if (INSTANCE == null) {
                synchronized(AppDatabase::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = databaseBuilder(
                            context.applicationContext,
                            AppDatabase::class.java, "game-db"
                        ).build()
                    }
                }
            }
            return INSTANCE!!
        }

        fun getExecutor(): Executor {
            if (DATABASE_WRITE_EXECUTOR == null) {
                synchronized(AppDatabase::class.java) {
                    if (DATABASE_WRITE_EXECUTOR == null) {
                        DATABASE_WRITE_EXECUTOR = Executors.newSingleThreadExecutor()
                    }
                }
            }
            return DATABASE_WRITE_EXECUTOR!!
        }
    }
}
