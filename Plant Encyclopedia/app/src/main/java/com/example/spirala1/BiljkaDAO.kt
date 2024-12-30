package com.example.spirala1

import android.graphics.Bitmap
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Dao
interface BiljkaDAO {

    @Insert
    suspend fun insertBiljka(biljka: Biljka)
    @Insert
    suspend fun saveBiljka(biljka: Biljka): Boolean = withContext(Dispatchers.IO){
        var listaSpasenih = getAllBiljkas()
        var postojiVec:Boolean = false
        for (pojedinacna in listaSpasenih){
            if (pojedinacna.naziv == biljka.naziv
                && pojedinacna.porodica == biljka.porodica) postojiVec=true
        }
        if (!postojiVec){
            try{
                insertBiljka(biljka)
            }
            catch(e:Exception){
                return@withContext false
            }
            return@withContext true
        }
        return@withContext false
        //pozovi getalbiljkas u neku listu spasi i provjeravaj sve osim id da li se podudara, ako se podudari retrunaj false tj ne dozvoli da se doda
        //a tmo kod savebiljkas staviti da se poziva nad svim biljkama
    }

    @Query("SELECT * FROM Biljka WHERE onlineChecked = 0")
    suspend fun getOfflineBiljke(): List<Biljka>
    @Transaction
    suspend fun fixOfflineBiljka(): Int = withContext(Dispatchers.IO){
        //val biljke = getAllBiljkas().filter { !it.onlineChecked }
        val biljke = getOfflineBiljke()
        var brojAzuriranih = 0

        for (biljka in biljke) {
            val original = biljka.copy()
            var nova = TrefleDAO().fixData(original)
            if (nova != original) {
                saveBiljka(nova)
                brojAzuriranih++
            }
        }

        return@withContext brojAzuriranih
    }

    @Query("SELECT * FROM BiljkaBitmap WHERE idBiljke = :idBiljke")
    suspend fun getBiljkaBitmap(idBiljke: Long): BiljkaBitmap?
    @Query("SELECT * FROM Biljka WHERE id = :id")
    suspend fun getBiljkaByID(id: Long): Biljka?
    @Insert
    suspend fun insertInBiljkaBitmap(biljkaBitmapa: BiljkaBitmap)
    @Insert
    suspend fun addImage(idBiljke : Long, bitmap: Bitmap): Boolean = withContext(Dispatchers.IO){
        try{
            if (getBiljkaByID(idBiljke) == null) return@withContext false
            if (getBiljkaBitmap(idBiljke) != null) return@withContext false
            var bb=BiljkaBitmap(idBiljke=idBiljke, bitmap=bitmap)
            insertInBiljkaBitmap(bb)
            return@withContext true
        }
        catch(e: Exception){
            return@withContext false
        }
    }

    @Query("SELECT * FROM Biljka")
    suspend fun getAllBiljkas(): List<Biljka>

    @Query("DELETE FROM Biljka")
    suspend fun clearAllBiljkas()

    @Query("DELETE FROM BiljkaBitmap")
    suspend fun clearAllBiljkaBitmaps()

    suspend fun clearData() {
        clearAllBiljkas()
        clearAllBiljkaBitmaps()
    }









}