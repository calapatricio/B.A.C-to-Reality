package com.cala.bactoreality

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Bebida::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun bebidaDao() : BebidaDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "base_de_datos_bebidas"
                ).build().also { INSTANCE = it }
            }
        }
    }
}