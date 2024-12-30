package com.example.spirala1

import androidx.room.TypeConverter

class ProfilOkusaConverter {
    @TypeConverter
    fun fromStoredString(value: String): ProfilOkusaBiljke {
        return ProfilOkusaBiljke.valueOf(value)
    }

    @TypeConverter
    fun toStoredString(profilOkusa: ProfilOkusaBiljke): String {
        return profilOkusa.name //opis ili name? --name
    }
}
