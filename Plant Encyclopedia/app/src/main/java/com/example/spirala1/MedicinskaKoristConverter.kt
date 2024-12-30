package com.example.spirala1

import androidx.room.TypeConverter
import java.util.Arrays

class MedicinskaKoristConverter {
    @TypeConverter
    fun fromStoredString(value: String): List<MedicinskaKorist> {
        val dbValues = Arrays.asList(*value.split("\\s*,\\s*".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray())
        //razdvajanje ulaznog stringa na dijelove koji su odvojeni zarezima, uklanjajuci praznine oko zareza
        val enums: MutableList<MedicinskaKorist> = mutableListOf()
        for (s in dbValues) {
            enums.add(MedicinskaKorist.valueOf(s!!))
        }
        return enums
    }

    @TypeConverter
    fun toStoredString(listaMedicinskihKoristi: List<MedicinskaKorist>): String {
        val value = StringBuilder()
        for (korist in listaMedicinskihKoristi) {
            value.append(korist.name).append(",")
        }
        if (value.length > 0) {
            value.setLength(value.length - 1)
        }
        return value.toString()
    }
}
