package com.example.spirala1

import androidx.room.TypeConverter
import java.util.Arrays

class KlimatskiTipConverter {
    @TypeConverter
    fun fromStoredString(value: String): List<KlimatskiTip> {
        val dbValues = Arrays.asList(*value.split("\\s*,\\s*".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray())
        //razdvajanje ulaznog stringa na dijelove koji su odvojeni zarezima, uklanjajuci praznine oko zareza
        val enums: MutableList<KlimatskiTip> = mutableListOf()
        for (s in dbValues) {
            enums.add(KlimatskiTip.valueOf(s!!))
        }
        return enums
    }

    @TypeConverter
    fun toStoredString(listaKlimatskihTipova: List<KlimatskiTip>): String {
        val value = StringBuilder()
        for (klimatskiTip in listaKlimatskihTipova) {
            value.append(klimatskiTip.name).append(",")
        }
        if (value.length > 0) {
            value.setLength(value.length - 1)
        }
        return value.toString()
    }
}
