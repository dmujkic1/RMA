package com.example.spirala1

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
//import androidx.room.vo.Entity
import java.io.Serializable
@Entity(tableName = "Biljka")
data class Biljka(
    @PrimaryKey(autoGenerate = true) val id: Long?=null,
    @ColumnInfo(name="naziv") val naziv : String,
    @ColumnInfo(name="family") val porodica : String,
    @ColumnInfo(name="medicinskoUpozorenje") val medicinskoUpozorenje : String,
    @TypeConverters(MedicinskaKoristConverter::class) val medicinskeKoristi : List<MedicinskaKorist>,
    @TypeConverters(ProfilOkusaConverter::class) val profilOkusa : ProfilOkusaBiljke?,
    @TypeConverters(ListaJelaConverter::class) val jela : List<String>,
    @TypeConverters(KlimatskiTipConverter::class) val klimatskiTipovi : List<KlimatskiTip>,
    @TypeConverters(ZemljisniTipConverter::class) val zemljisniTipovi : List<Zemljište>,
    @ColumnInfo(name="onlineChecked") var onlineChecked: Boolean=false
): Serializable