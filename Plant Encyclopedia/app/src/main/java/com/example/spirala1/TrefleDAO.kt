package com.example.spirala1

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.bumptech.glide.Glide
import com.example.spirala1.APIAdapter.retrofit
import com.google.gson.internal.bind.TypeAdapters.URL
import java.net.URL
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class TrefleDAO {
    private lateinit var context: Context
    private lateinit var biljkaDAO: BiljkaDAO
    fun setContext(context: Context){
        this.context=context
        biljkaDAO = BiljkaDatabase.getDatabase(context).biljkaDao()
    }
    fun izvuciLatinskiNaziv(naziv: String): String?{
        val startIndex = naziv.indexOf('(')
        val endIndex = naziv.indexOf(')')
        return if (startIndex != -1 && endIndex != -1 && startIndex < endIndex) {
            naziv.substring(startIndex + 1, endIndex)
        } else {
            null
        }
    }
    private fun getDefaultBitmap(): Bitmap{
        return BitmapFactory.decodeResource(context.resources, R.drawable.defaultplant)
    }

    suspend fun getImage(biljka: Biljka): Bitmap = withContext(Dispatchers.IO) {
        try {
            val latinskiNaziv = izvuciLatinskiNaziv(biljka.naziv) ?: String
            val response = APIAdapter.retrofit.searchPlants(latinskiNaziv.toString(), "FfSlLp0DzAZlPT8UTdn2tPEBPmuLPeA6C0eLMpkAFRY")
            if (response.isSuccessful){
                val biljke = response.body()?.data
                if (!biljke.isNullOrEmpty()){
                    val imageURL = biljke[0].image_url
                    if (!imageURL.isNullOrEmpty()){
                        val inputStream = URL(imageURL).openStream()
                        return@withContext BitmapFactory.decodeStream(inputStream)
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return@withContext getDefaultBitmap()
    }

    suspend fun fixData(biljka: Biljka): Biljka = withContext(Dispatchers.IO){
        var naziv : String = biljka.naziv
        var porodica = biljka.porodica
        var medicinskoUpozorenje : String = biljka.medicinskoUpozorenje
        var medicinskeKoristi : List<MedicinskaKorist> = biljka.medicinskeKoristi
        var profilOkusa : ProfilOkusaBiljke? = biljka.profilOkusa
        var jela : MutableList<String> = biljka.jela.toMutableList()
        var klimatskiTipovi : MutableList<KlimatskiTip> = biljka.klimatskiTipovi.toMutableList()
        var zemljisniTipovi : MutableList<Zemljište> = biljka.zemljisniTipovi.toMutableList()
        var tempBiljka=Biljka(null,"","","", emptyList(),null, emptyList(), emptyList(), emptyList())
        try{
            val latinskiNaziv = izvuciLatinskiNaziv(biljka.naziv) ?: String
            val response = APIAdapter.retrofit.searchPlants(latinskiNaziv.toString())
            if (response.isSuccessful) {
                val plantResponse = response.body()?.data?.firstOrNull()
                if (plantResponse != null) {
                    // Ažuriranje porodice biljke
                    if (plantResponse.family != biljka.porodica) {
                        porodica = plantResponse.family
                    }
                    // Provjera i ažuriranje jela
                    val id:Int = plantResponse.id
                    val responseDetail = APIAdapter.retrofit.getPlantDetails(id)
                    var novoime=plantResponse.common_name
                    var latinskoIme=plantResponse.scientific_name
                    if (responseDetail.isSuccessful)
                    {
                        val biljkadetalji=responseDetail.body()?.data
                        if(biljkadetalji?.main_species?.edible!=null && biljkadetalji.main_species?.edible==false){
                            //ukini jela i dodaj medupoz da nije jestivo
                            jela.clear()
                            val staritekst=medicinskoUpozorenje
                            val novitekst=" NIJE JESTIVO"
                            medicinskoUpozorenje=staritekst+novitekst
                        }
                        //provjera medupoz
                        if (biljkadetalji?.main_species?.specifications?.toxicity!=null && biljkadetalji?.main_species?.specifications?.toxicity!="none" && !medicinskoUpozorenje.contains("TOKSIČNO")){
                            //dodaj TOKSICNO
                            val staritekst=biljkadetalji?.main_species?.specifications?.toxicity
                            val novitekst=" TOKSIČNO"
                            medicinskoUpozorenje=staritekst+novitekst
                        }

                        // Provjera i ažuriranje zemljisnih tipova
                        var listazemlje : MutableList<Zemljište> = biljka.zemljisniTipovi.toMutableList()
                        if (biljkadetalji?.main_species?.growth?.soil_texture!=null) {
                            listazemlje.clear()
                            val tekstura= biljkadetalji?.main_species?.growth?.soil_texture
                            when (tekstura){
                                in 1..2 -> listazemlje.add(Zemljište.GLINENO)
                                in 3..4 -> listazemlje.add(Zemljište.PJESKOVITO)
                                in 5..6 -> listazemlje.add(Zemljište.ILOVACA)
                                in 7..8 -> listazemlje.add(Zemljište.CRNICA)
                                in 9..9 -> listazemlje.add(Zemljište.SLJUNKOVITO)
                                in 10..10 -> listazemlje.add(Zemljište.KRECNJACKO)
                            }
                            zemljisniTipovi=listazemlje
                        }

                        // Provjera i ažuriranje klime
                        // vec definisano var klimatskiTipovi = mutableListOf<KlimatskiTip>()
                        val svjetlost = biljkadetalji?.main_species?.growth?.light
                        val vlaznost = biljkadetalji?.main_species?.growth?.atmospheric_humidity
                        if (svjetlost!=null && vlaznost!=null) {
                            klimatskiTipovi.clear()
                            if (svjetlost>=6 && svjetlost<=9 && vlaznost>=1 && vlaznost<=5) { klimatskiTipovi.add(KlimatskiTip.SREDOZEMNA) }
                            if (svjetlost>=8 && svjetlost<=10 && vlaznost>=7 && vlaznost<=10) { klimatskiTipovi.add(KlimatskiTip.TROPSKA) }
                            if (svjetlost>=6 && svjetlost<=9 && vlaznost>=5 && vlaznost<=8) { klimatskiTipovi.add(KlimatskiTip.SUBTROPSKA) }
                            if (svjetlost>=4 && svjetlost<=7 && vlaznost>=3 && vlaznost<=7) { klimatskiTipovi.add(KlimatskiTip.UMJERENA) }
                            if (svjetlost>=7 && svjetlost<=9 && vlaznost>=1 && vlaznost<=2) { klimatskiTipovi.add(KlimatskiTip.SUHA) }
                            if (svjetlost>=0 && svjetlost<=5 && vlaznost>=3 && vlaznost<=7) { klimatskiTipovi.add(KlimatskiTip.PLANINSKA) }
                        }

                        novoime=novoime.plus(" ($latinskoIme)")
                        tempBiljka=Biljka(biljka.id,novoime,porodica,medicinskoUpozorenje,medicinskeKoristi,profilOkusa,jela,klimatskiTipovi,zemljisniTipovi)
                        //Log.d("vracena", tempBiljka.toString())
                        //return@withContext Biljka(naziv=naziv, porodica=porodica, medicinskoUpozorenje=medicinskoUpozorenje, medicinskeKoristi=medicinskeKoristi, profilOkusa=profilOkusa, jela=jela, klimatskiTipovi=klimatskiTipovi, zemljisniTipovi=zemljisniTipovi)
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        val biljkaBitmap = biljkaDAO.getBiljkaBitmap(tempBiljka.id?:0)
        if (biljkaBitmap == null) {
            val bitmap = getImage(tempBiljka)
            biljkaDAO.addImage(tempBiljka.id?:0, bitmap)
        }
        //Log.d("vracena", tempBiljka.toString())
        return@withContext tempBiljka
    }

    //vraća listu biljaka koje imaju boju cvijeta flower_color i sadrže podstring
    // substr unutar "scientific_name". Sve atribute biljaka koje ne možete dobiti
    // sa web servisa ili koji su null postavite na null ili na prazan string
    suspend fun getPlantsWithFlowerColor(flower_color: String, substr: String): List<Biljka> = withContext(Dispatchers.IO) {
        var listaBiljaka: MutableList<Biljka> = mutableListOf()
        try {
            val filters=mapOf("filter[flower_color]" to flower_color)
            val responseColor=APIAdapter.retrofit.getPlantsByFlowerColorAPI(substr,filters)
            var id:Int
            var listaID: MutableList<Int> = mutableListOf()
            var ime:String
            var familija:String
            if (responseColor.isSuccessful){
                val lista=responseColor.body()?.data
                //val plantResponseColor = responseColor.body()?.data?.firstOrNull()
                //if (plantResponseColor!=null){
                if (!lista.isNullOrEmpty()){
                    for (biljka in lista){
                        id=biljka.id
                        familija=biljka.family
                        ime= biljka.common_name.toString() + " (" + biljka.scientific_name + ")"

                        val responseDetail = APIAdapter.retrofit.getPlantDetails(id)
                        if (responseDetail.isSuccessful){
                            val biljkadetalji=responseDetail.body()?.data
                            //PROVJERITI
                            if ((biljka.scientific_name!=null && biljka.scientific_name.lowercase().contains(substr))
                                || (biljka.common_name!=null && biljka.common_name.lowercase().contains(substr)) ){

                                // Provjera i ažuriranje zemljisnih tipova
                                var listazemlje = mutableListOf<Zemljište>()
                                if (biljkadetalji?.main_species?.growth?.soil_texture!=null) {
                                    val tekstura= biljkadetalji?.main_species?.growth?.soil_texture
                                    when (tekstura){
                                        in 1..2 -> listazemlje.add(Zemljište.GLINENO)
                                        in 3..4 -> listazemlje.add(Zemljište.PJESKOVITO)
                                        in 5..6 -> listazemlje.add(Zemljište.ILOVACA)
                                        in 7..8 -> listazemlje.add(Zemljište.CRNICA)
                                        in 9..9 -> listazemlje.add(Zemljište.SLJUNKOVITO)
                                        in 10..10 -> listazemlje.add(Zemljište.KRECNJACKO)
                                    }
                                }
                                // Provjera i ažuriranje klime
                                // vec definisano var klimatskiTipovi = mutableListOf<KlimatskiTip>()
                                var klimatskiTipovi = mutableListOf<KlimatskiTip>()
                                val svjetlost = biljkadetalji?.main_species?.growth?.light
                                val vlaznost = biljkadetalji?.main_species?.growth?.atmospheric_humidity
                                if (svjetlost!=null && vlaznost!=null) {
                                    if (svjetlost>=6 && svjetlost<=9 && vlaznost>=1 && vlaznost<=5) { klimatskiTipovi.add(KlimatskiTip.SREDOZEMNA) }
                                    if (svjetlost>=8 && svjetlost<=10 && vlaznost>=7 && vlaznost<=10) { klimatskiTipovi.add(KlimatskiTip.TROPSKA) }
                                    if (svjetlost>=6 && svjetlost<=9 && vlaznost>=5 && vlaznost<=8) { klimatskiTipovi.add(KlimatskiTip.SUBTROPSKA) }
                                    if (svjetlost>=4 && svjetlost<=7 && vlaznost>=3 && vlaznost<=7) { klimatskiTipovi.add(KlimatskiTip.UMJERENA) }
                                    if (svjetlost>=7 && svjetlost<=9 && vlaznost>=1 && vlaznost<=2) { klimatskiTipovi.add(KlimatskiTip.SUHA) }
                                    if (svjetlost>=0 && svjetlost<=5 && vlaznost>=3 && vlaznost<=7) { klimatskiTipovi.add(KlimatskiTip.PLANINSKA) }
                                }
                                val nova = Biljka(null,ime,familija, "", emptyList(),null, emptyList(), klimatskiTipovi, listazemlje)
                                //Log.d("nova", nova.toString())
                                listaBiljaka.add(nova)
                            }
                        }
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return@withContext listaBiljaka
    }



}