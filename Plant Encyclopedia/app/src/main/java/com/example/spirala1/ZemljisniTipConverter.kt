package com.example.spirala1

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.Arrays

class ZemljisniTipConverter {
    @TypeConverter
    fun fromStoredString(value: String): List<Zemljište> {
        val dbValues = Arrays.asList(*value.split("\\s*,\\s*".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray())
        //razdvajanje ulaznog stringa na dijelove koji su odvojeni zarezima, uklanjajuci praznine oko zareza
        val enums: MutableList<Zemljište> = mutableListOf()
        for (s in dbValues) {
            enums.add(Zemljište.valueOf(s!!))
        }
        return enums
    }

    @TypeConverter
    fun toStoredString(listaZemljisnihTipova: List<Zemljište>): String {
        val value = StringBuilder()
        for (zemljisniTip in listaZemljisnihTipova) {
            value.append(zemljisniTip.name).append(",")
        }
        if (value.length > 0) {
            value.setLength(value.length - 1)
        }
        return value.toString()
    }
}
