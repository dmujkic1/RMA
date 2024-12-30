package com.example.spirala1

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DatabaseTest {
    private lateinit var biljkaDao: BiljkaDAO
    private lateinit var db: BiljkaDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, BiljkaDatabase::class.java).build()
        biljkaDao = db.biljkaDao()
    }

    @After
    fun closeDb() {
        if (::db.isInitialized) {
            db.close()
        }
    }

    @Test
    fun testDodajBiljku() = runBlocking {
        val biljka = Biljka(1, "Rose", "Rosaceae", "Rosa", listOf(MedicinskaKorist.REGULACIJAPRITISKA), ProfilOkusaBiljke.SLATKI, listOf("cevapi", "sarma"), listOf(KlimatskiTip.SUHA), listOf(Zemljište.KRECNJACKO), true)
        biljkaDao.saveBiljka(biljka)

        val sveBiljke = biljkaDao.getAllBiljkas()
        assertEquals(1, sveBiljke.size)
        assertEquals(biljka, sveBiljke[0])
    }

    @Test
    fun testObrisiSveBiljke() = runBlocking {
        val biljka = Biljka(1, "Rose", "Rosaceae", "Rosa", listOf(MedicinskaKorist.REGULACIJAPRITISKA), ProfilOkusaBiljke.SLATKI, listOf("cevapi", "sarma"), listOf(KlimatskiTip.SUHA), listOf(Zemljište.KRECNJACKO), true)
        biljkaDao.saveBiljka(biljka)

        biljkaDao.clearData()

        val sveBiljke = biljkaDao.getAllBiljkas()
        assertTrue(sveBiljke.isEmpty())
    }
}