package com.calcprojects.constructorbuddy.data.db

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.calcprojects.constructorbuddy.model.Model

@androidx.room.Database(entities = [Model::class], version = 1)
abstract class Database : RoomDatabase() {

    abstract fun getModelDao(): ModelDao

    companion object {
        @Volatile
        private var instance: Database? = null

        operator fun invoke(context: Context): Database {
            return instance ?: synchronized(this) {
                val newInstance = Room.databaseBuilder(
                    context.applicationContext,
                    Database::class.java,
                    "CONSTRUCTOR_BUDDY_DB"
                ).build()
                instance = newInstance
                return newInstance
            }
        }
    }
}