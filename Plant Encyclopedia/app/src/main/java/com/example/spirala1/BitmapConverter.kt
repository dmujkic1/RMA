package com.example.spirala1

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import androidx.room.TypeConverter
import java.io.ByteArrayOutputStream
import kotlin.math.min

class BitmapConverter {
    @TypeConverter
    fun fromBitmap(bitmap: Bitmap): String {
        val maxWidth = 800
        val maxHeight = 600

        val scaleFactor = min(maxWidth.toFloat() / bitmap.width, maxHeight.toFloat() / bitmap.height)
        val newWidth = (bitmap.width * scaleFactor).toInt()
        val newHeight = (bitmap.height * scaleFactor).toInt()

        val resizedBitmap = Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true)
        val outputStream = ByteArrayOutputStream()
        resizedBitmap.compress(Bitmap.CompressFormat.PNG, 50, outputStream)
        val byteArray = outputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    @TypeConverter
    fun toBitmap(base64String: String): Bitmap {
        val byteArray = Base64.decode(base64String, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    }
}
