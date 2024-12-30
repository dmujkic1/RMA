package com.example.spirala1

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.Serializable

class MainActivity : AppCompatActivity() {
    private var trefle=TrefleDAO()
    private lateinit var context: Context //iskoristit novu listu dobivenu sa getplantswithflowercolor
    private lateinit var medicinskiAdapter: MedicinskiAdapter
    private lateinit var botanickiAdapter: BotaničkiAdapter
    private lateinit var kuharskiAdapter: KuharskiAdapter
    private var biljke: MutableList<Biljka> = mutableListOf<Biljka>().apply { addAll(getBiljke()) }
    private lateinit var recyclerView: RecyclerView
    private lateinit var spinner: Spinner
    private var trenutnaFiltriranaLista: List<Biljka>?=null
    private lateinit var resetDugme: Button
    private lateinit var novaBiljkaBtn: Button
    private val NOVA_BILJKA_REQUEST_CODE = 101
    private lateinit var bojaSPIN: Spinner
    private lateinit var pretragaET: EditText
    private lateinit var brzaPretraga: Button
    private lateinit var linearniZaPretragu: LinearLayout
    private var boje = getBoje()
    private lateinit var boja: String
    private lateinit var listaBiljakaPretaga: MutableList<Biljka>
    private lateinit var biljkaDAO: BiljkaDAO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val database = BiljkaDatabase.getDatabase(this)
        biljkaDAO = database.biljkaDao()

        bojaSPIN = findViewById(R.id.bojaSPIN)
        pretragaET = findViewById(R.id.pretragaET)
        linearniZaPretragu = findViewById(R.id.BotanskiHedonizam)
        val bojaSPINAdapter=ArrayAdapter(this, android.R.layout.simple_spinner_item,boje)
        bojaSPINAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        bojaSPIN.adapter=bojaSPINAdapter

        novaBiljkaBtn = findViewById(R.id.novaBiljkaBtn)
        novaBiljkaBtn.setOnClickListener{
            val intent = Intent(this,NovaBiljkaActivity::class.java)
            startActivity(intent)
        }

        brzaPretraga = findViewById(R.id.brzaPretraga)
        brzaPretraga.setOnClickListener {
            trefle.setContext(this)
            val scope = CoroutineScope(Job() + Dispatchers.Main)
            if (pretragaET.text.toString()!="") scope.launch {
                listaBiljakaPretaga=trefle.getPlantsWithFlowerColor(boja,pretragaET.text.toString()).toMutableList()
                botanickiAdapter.postaviNaTruePretragu()
                for (biljkaVracena in listaBiljakaPretaga) biljkaDAO.saveBiljka(biljkaVracena)
                botanickiAdapter.updateBiljke(listaBiljakaPretaga)
            }
        }

        resetDugme = findViewById(R.id.resetBtn)
        resetDugme.setOnClickListener {
            trenutnaFiltriranaLista=null
            medicinskiAdapter.updateBiljke(biljke)
            kuharskiAdapter.updateBiljke(biljke)
            botanickiAdapter.updateBiljke(biljke)
            val scope = CoroutineScope(Job()+Dispatchers.Main)
            scope.launch { biljkaDAO.clearData() }
            botanickiAdapter.postaviNaFalsePretragu()
        }

        spinner = findViewById(R.id.modSpinner)
        val lista=listOf("Medicinski","Kuharski","Botanički")
        val adapterSpinner=ArrayAdapter(this,android.R.layout.simple_spinner_item,lista)
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
        spinner.adapter=adapterSpinner

        medicinskiAdapter = MedicinskiAdapter(this, biljke) {biljka -> onItemClick(biljka)}
        kuharskiAdapter = KuharskiAdapter(this, biljke) {biljka -> onItemClick(biljka)}
        botanickiAdapter = BotaničkiAdapter(this, biljke) {biljka -> onItemClick(biljka)}

        recyclerView = findViewById(R.id.biljkeRV)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = medicinskiAdapter

        bojaSPIN.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            @SuppressLint("SuspiciousIndentation")
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long){
                val selectedOption: String = parent?.getItemAtPosition(position).toString()
                boja=selectedOption
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                boja=""
            }
        }

        spinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long) {
                when (position) {
                    0 -> {
                        botanickiAdapter.postaviNaFalsePretragu()
                        recyclerView.setPadding(0,0,0,spinner.height)
                        linearniZaPretragu.visibility=View.GONE
                        pretragaET.setText("")
                        recyclerView.adapter = medicinskiAdapter
                        if (trenutnaFiltriranaLista != null) {
                            medicinskiAdapter.updateBiljke(trenutnaFiltriranaLista!!)
                        }
                        else{
                            val scope = CoroutineScope(Job() + Dispatchers.Main)
                            scope.launch{
                                for (spasiti in biljke){
                                    biljkaDAO.saveBiljka(spasiti)
                                }
                                biljke=getAllBiljkas().toMutableList()
                                medicinskiAdapter.updateBiljke(biljke)
                            }

                        }
                    }
                    1 -> {
                        botanickiAdapter.postaviNaFalsePretragu()
                        recyclerView.setPadding(0,0,0,spinner.height)
                        linearniZaPretragu.visibility=View.GONE
                        pretragaET.setText("")
                        recyclerView.adapter = kuharskiAdapter
                        if (trenutnaFiltriranaLista != null) {
                            kuharskiAdapter.updateBiljke(trenutnaFiltriranaLista!!)
                        }
                        else{
                            val scope = CoroutineScope(Job() + Dispatchers.Main)
                            scope.launch {
                                for (spasiti in biljke){
                                    biljkaDAO.saveBiljka(spasiti)
                                }
                                biljke=getAllBiljkas().toMutableList()
                                kuharskiAdapter.updateBiljke(biljke)
                            }

                        }
                    }
                    2 -> {
                        recyclerView.setPadding(0,0,0,spinner.height+linearniZaPretragu.height)
                        linearniZaPretragu.visibility=View.VISIBLE
                        recyclerView.adapter = botanickiAdapter
                        if (trenutnaFiltriranaLista != null) {
                            botanickiAdapter.updateBiljke(trenutnaFiltriranaLista!!)
                        }
                        else{
                            val scope = CoroutineScope(Job() + Dispatchers.Main)
                            scope.launch{
                                for (spasiti in biljke){
                                    biljkaDAO.saveBiljka(spasiti)
                                }
                                biljke=getAllBiljkas().toMutableList()
                                botanickiAdapter.updateBiljke(biljke)
                            }

                        }
                    }
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                recyclerView.adapter = medicinskiAdapter
            }

        }
        medicinskiAdapter?.updateBiljke(biljke)
        kuharskiAdapter?.updateBiljke(biljke)
        botanickiAdapter?.updateBiljke(biljke)

        //DODAVANJE BILJKE U LISTU
        val podaci=intent.extras
        if(podaci!=null && podaci.containsKey("novaBiljka"))
        {
            val novaBiljka = intent.getSerializableExtra("novaBiljka") as Biljka
            dodavanjeNoveBiljke(novaBiljka)
            biljke= getBiljke()
            medicinskiAdapter?.updateBiljke(biljke)
            kuharskiAdapter?.updateBiljke(biljke)
            botanickiAdapter?.updateBiljke(biljke)
        }
    }

    private fun onItemClick(selectedBiljka: Biljka) {
        if (!botanickiAdapter.getPretragaBool()){
            val filtriranaLista = when (spinner.selectedItemPosition) {
                0 -> filtrirajMedicinski(selectedBiljka)
                1 -> filtrirajKuharski(selectedBiljka)
                2 -> filtrirajBotanicki(selectedBiljka)
                else -> emptyList()
            }
            trenutnaFiltriranaLista=filtriranaLista
            when (spinner.selectedItemPosition) {
                0 -> medicinskiAdapter.updateBiljke(filtriranaLista)
                1 -> kuharskiAdapter.updateBiljke(filtriranaLista)
                2 -> botanickiAdapter.updateBiljke(filtriranaLista)
            }
        }
    }


    private fun filtrirajKuharski(odabranaBiljka: Biljka): List<Biljka> {
        return biljke.filter { biljka ->
            biljka.jela.intersect(odabranaBiljka.jela).isNotEmpty() ||
                    biljka.profilOkusa == odabranaBiljka.profilOkusa
        }
    }
    private fun filtrirajMedicinski(odabranaBiljka: Biljka): List<Biljka> {
        return biljke.filter { biljka ->
            biljka.medicinskeKoristi.intersect(odabranaBiljka.medicinskeKoristi).isNotEmpty()
        }
    }
    private fun filtrirajBotanicki(odabranaBiljka: Biljka): List<Biljka> {
        if (botanickiAdapter.getPretragaBool()) return listaBiljakaPretaga
        return biljke.filter { biljka ->
            biljka.porodica == odabranaBiljka.porodica &&
                    biljka.klimatskiTipovi.intersect(odabranaBiljka.klimatskiTipovi).isNotEmpty() &&
                    biljka.zemljisniTipovi.intersect(odabranaBiljka.zemljisniTipovi).isNotEmpty()
        }
    }
    private suspend fun fixData(biljka: Biljka): Biljka{
        val fixedBiljka = trefle.fixData(biljka)
        return fixedBiljka
    }

    private suspend fun getAllBiljkas(): List<Biljka> {
        return biljkaDAO.getAllBiljkas()
    }

}