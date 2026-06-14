package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [CscOrder::class], version = 1, exportSchema = false)
abstract class CscDatabase : RoomDatabase() {
    abstract fun cscOrderDao(): CscOrderDao

    companion object {
        @Volatile
        private var INSTANCE: CscDatabase? = null

        fun getDatabase(context: Context): CscDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CscDatabase::class.java,
                    "csc_database"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
