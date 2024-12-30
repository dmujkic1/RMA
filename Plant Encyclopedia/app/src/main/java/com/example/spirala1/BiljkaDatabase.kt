package com.example.spirala1

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [Biljka::class, BiljkaBitmap::class], version = 1)
@TypeConverters(MedicinskaKoristConverter::class, ListaJelaConverter::class, BitmapConverter::class, KlimatskiTipConverter::class, ProfilOkusaConverter::class, ZemljisniTipConverter::class)
abstract class BiljkaDatabase : RoomDatabase() {
    abstract fun biljkaDao(): BiljkaDAO
    companion object {
        @Volatile private var INSTANCE: BiljkaDatabase? = null

        fun getDatabase(context: Context): BiljkaDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BiljkaDatabase::class.java,
                    "biljke-db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}