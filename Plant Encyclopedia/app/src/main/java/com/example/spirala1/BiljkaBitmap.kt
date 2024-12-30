package com.example.spirala1

import android.graphics.Bitmap
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.TypeConverters

@Entity(tableName = "BiljkaBitmap",
    foreignKeys = [ForeignKey(entity = Biljka::class,
        parentColumns = ["id"],
        childColumns = ["idBiljke"],
        onDelete = ForeignKey.CASCADE)]) //obrisi sve za biljku

data class BiljkaBitmap(
    @PrimaryKey(autoGenerate = true) val id: Long?=null,
    @ColumnInfo(name="idBiljke") val idBiljke: Long,
    @TypeConverters(BitmapConverter::class) val bitmap: Bitmap
)
