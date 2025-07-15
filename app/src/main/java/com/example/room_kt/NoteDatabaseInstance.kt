package com.example.room_kt

import android.content.Context
import androidx.room.Room

object NoteDatabaseInstance {
    @Volatile
    private var INSTANCE: AppDatabase? = null

    fun getDatabase(context: Context): AppDatabase {
        return INSTANCE ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "note_database"
            ).build()
            INSTANCE = instance
            instance
        }
    }
}