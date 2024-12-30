package com.example.spirala1

import androidx.room.TypeConverter
import java.util.Arrays

class ListaJelaConverter {
    @TypeConverter
    fun fromStoredString(value: String): List<String> {
        val dbValues = Arrays.asList(*value.split("\\s*,\\s*".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray())
        //razdvajanje ulaznog stringa na dijelove koji su odvojeni zarezima, uklanjajuci praznine oko zareza
        return dbValues.map { it.trim() }
    }

    @TypeConverter
    fun toStoredString(listaJela: List<String>): String {
        val value = StringBuilder()
        for (jelo in listaJela) {
            value.append(jelo).append(",")
        }
        if (value.length > 0) {
            value.setLength(value.length - 1)
        }
        return value.toString()
    }
}
