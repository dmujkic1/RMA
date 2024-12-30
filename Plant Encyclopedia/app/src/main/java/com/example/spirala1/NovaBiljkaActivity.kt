package com.example.spirala1

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ListView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isEmpty
import java.io.Serializable
import android.Manifest
import android.content.Context
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


class NovaBiljkaActivity : AppCompatActivity() {
    private val REQUEST_IMAGE_CAPTURE = 1
    private val CAMERA_PERMISSION_REQUEST_CODE = 2
    private var trefle=TrefleDAO()
    private lateinit var context: Context
    private lateinit var biljkaDAO: BiljkaDAO

    private lateinit var dodajJeloBtn : Button
    private lateinit var dodajBiljkuBtn : Button
    private lateinit var uslikajBiljkuBtn : Button
    private lateinit var slikaIV: ImageView
    private var capturedImage: Bitmap? = null
    private lateinit var jeloET: EditText
    private lateinit var medicinskoUpozorenjeET: EditText
    private lateinit var nazivET: EditText
    private lateinit var porodicaET: EditText
    private lateinit var jelaLV: ListView
    private lateinit var medicinskaKoristLV: ListView
    private lateinit var klimatskiTipLV: ListView
    private lateinit var zemljisniTipLV: ListView
    private lateinit var profilOkusaLV: ListView
    private var listaJela= mutableListOf<String>()
    private var listaKlima= getKlime()
    private var listaKoristi = getKoristi()
    private var listaZemlje = getZemlja()
    private var listaOkusa = getOkusi()
    private lateinit var adapterListaJela : ArrayAdapter<String>
    private lateinit var adapterListaMedicinskihKoristi : ArrayAdapter<String>
    private lateinit var adapterListaKlimatskihTipova : ArrayAdapter<String>
    private lateinit var adapterListaZemljisnihTipova : ArrayAdapter<String>
    private lateinit var adapterListaProfilaOkusa : ArrayAdapter<String>
    private val NOVA_BILJKA_REQUEST_CODE = 101
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_nova_biljka)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val database = BiljkaDatabase.getDatabase(this)
        biljkaDAO = database.biljkaDao()

        context=this
        dodajJeloBtn = findViewById(R.id.dodajJeloBtn)
        dodajBiljkuBtn = findViewById(R.id.dodajBiljkuBtn)
        uslikajBiljkuBtn = findViewById(R.id.uslikajBiljkuBtn)
        jeloET=findViewById(R.id.jeloET)
        medicinskoUpozorenjeET=findViewById(R.id.medicinskoUpozorenjeET)
        nazivET=findViewById(R.id.nazivET)
        porodicaET=findViewById(R.id.porodicaET)
        jelaLV=findViewById(R.id.jelaLV)
        medicinskaKoristLV=findViewById(R.id.medicinskaKoristLV)
        klimatskiTipLV=findViewById(R.id.klimatskiTipLV)
        zemljisniTipLV=findViewById(R.id.zemljisniTipLV)
        profilOkusaLV=findViewById(R.id.profilOkusaLV)

        adapterListaJela= ArrayAdapter(this, android.R.layout.simple_list_item_1,listaJela)
        jelaLV.adapter=adapterListaJela
        jelaLV.choiceMode=ListView.CHOICE_MODE_SINGLE

        adapterListaProfilaOkusa = ArrayAdapter(this, android.R.layout.simple_list_item_single_choice, listaOkusa)
        profilOkusaLV.adapter=adapterListaProfilaOkusa
        profilOkusaLV.choiceMode=ListView.CHOICE_MODE_SINGLE

        adapterListaKlimatskihTipova = ArrayAdapter(this, android.R.layout.simple_list_item_multiple_choice, listaKlima)
        klimatskiTipLV.adapter=adapterListaKlimatskihTipova
        klimatskiTipLV.choiceMode=ListView.CHOICE_MODE_MULTIPLE

        adapterListaMedicinskihKoristi = ArrayAdapter(this, android.R.layout.simple_list_item_multiple_choice, listaKoristi)
        medicinskaKoristLV.adapter=adapterListaMedicinskihKoristi
        medicinskaKoristLV.choiceMode=ListView.CHOICE_MODE_MULTIPLE

        adapterListaZemljisnihTipova = ArrayAdapter(this, android.R.layout.simple_list_item_multiple_choice, listaZemlje)
        zemljisniTipLV.adapter=adapterListaZemljisnihTipova
        zemljisniTipLV.choiceMode=ListView.CHOICE_MODE_MULTIPLE

        var odabraniIndeks=-1
        jelaLV.setOnItemClickListener { parent, view, position, id ->
            odabraniIndeks=position
            dodajJeloBtn.text="Izmijeni jelo"
            jeloET.setText(listaJela[odabraniIndeks])
        }

        dodajJeloBtn.setOnClickListener{
            val tekst = jeloET.text //tekst jela koji se dodaje ili mijenja
            val jeloPostojiVec = listaJela.any{it.compareTo(tekst.toString(), ignoreCase = true)==0}
            val tekstDugmeta=dodajJeloBtn.text
            if (tekstDugmeta.equals("Dodaj jelo") && odabraniIndeks==-1) {
                if (tekst.length < 2 || tekst.length > 20)
                {
                    jeloET.error="Netacan unos jela"
                }
                else
                if (jeloPostojiVec)
                {
                    jeloET.error="Jelo vec postoji"
                }
                else
                {
                    listaJela.add(tekst.toString())
                    adapterListaJela.notifyDataSetChanged()
                }
            }
            else if(odabraniIndeks!=-1)
            {
                if (tekst.isNotEmpty())
                {
                    if (tekst.length < 2 || tekst.length > 20)
                    {
                        jeloET.error="Netacan unos jela"
                    }
                    if (jeloPostojiVec)
                    {
                        jeloET.error="Jelo vec postoji"
                    }
                    else
                    {
                        listaJela[odabraniIndeks] = tekst.toString()
                        adapterListaJela.notifyDataSetChanged()
                        jeloET.setText("") // Postavi EditText na prazan string
                        dodajJeloBtn.text = "Dodaj jelo" // Vrati tekst dugmeta na početnu vrijednost
                    }
                }
                else
                {
                    listaJela.removeAt(odabraniIndeks)
                    adapterListaJela.notifyDataSetChanged()
                    jeloET.setText("") // Postavi EditText na prazan string
                    dodajJeloBtn.text = "Dodaj jelo" // Vrati tekst dugmeta na početnu vrijednost
                }
            }
        }
        dodajBiljkuBtn.setOnClickListener{
            if (validiraj()) //validno, isValid true
            {
                var novaBiljka = kreirajNovuBiljku()

                trefle.setContext(this)
                val scope = CoroutineScope(Job() + Dispatchers.Main)
                scope.launch {
                    novaBiljka=trefle.fixData(novaBiljka)
                    saveBiljka(novaBiljka)
                    val intent = Intent(context, MainActivity::class.java) // Custom action name
                    intent.putExtra("novaBiljka", novaBiljka as Serializable)
                    startActivity(intent)
                }


            }
        }
        uslikajBiljkuBtn = findViewById(R.id.uslikajBiljkuBtn)
        slikaIV = findViewById(R.id.slikaIV)
        uslikajBiljkuBtn.setOnClickListener {
            if (checkCameraPermission()) {
                dispatchTakePictureIntent()
            } else {
                requestCameraPermission()
            }
        }

       // uslikajBiljkuBtn.setOnClickListener(View.OnClickListener { dispatchTakePictureIntent() })

    }

    private fun checkCameraPermission(): Boolean {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestCameraPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array
    <String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                dispatchTakePictureIntent()
            }
        }
    }

    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(packageManager) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val extras: Bundle? = data?.extras
            extras?.let {
                val imageBitmap = extras.get("data") as Bitmap
                slikaIV.setImageBitmap(imageBitmap)
            }
        }
    }
    private fun kreirajNovuBiljku(): Biljka {
        val listaMedicinskihKoristi=mutableListOf<MedicinskaKorist>()
        val listaKlimatskihTipova = mutableListOf<KlimatskiTip>()
        val listaZemljisnihTipova = mutableListOf<Zemljište>()
        val listaProfilOkusa = mutableListOf<ProfilOkusaBiljke>()

        for (i in 0 until medicinskaKoristLV.adapter.count) {
            if (medicinskaKoristLV.isItemChecked(i)) listaMedicinskihKoristi.add(enumValues<MedicinskaKorist>()[i])
        }
        for (i in 0 until klimatskiTipLV.adapter.count) {
            if (klimatskiTipLV.isItemChecked(i)) listaKlimatskihTipova.add(enumValues<KlimatskiTip>()[i])
        }
        for (i in 0 until zemljisniTipLV.adapter.count) {
            if (zemljisniTipLV.isItemChecked(i)) listaZemljisnihTipova.add(enumValues<Zemljište>()[i])
        }
        for (i in 0 until profilOkusaLV.adapter.count) {
            if (profilOkusaLV.isItemChecked(i)) listaProfilOkusa.add(enumValues<ProfilOkusaBiljke>()[i])
        }

        return Biljka(
            naziv = nazivET.text.toString(),
            porodica = porodicaET.text.toString(),
            medicinskoUpozorenje = medicinskoUpozorenjeET.text.toString(),
            medicinskeKoristi = listaMedicinskihKoristi,
            profilOkusa = listaProfilOkusa.get(0),
            jela = listaJela,
            klimatskiTipovi = listaKlimatskihTipova,
            zemljisniTipovi = listaZemljisnihTipova
        )
    }


    private fun validiraj():Boolean{ //grah i Grah cu validirati gore prilikom unosa a sve ostale ovdje
        var isValid = true
        if (!nazivET.text.contains("(") || !nazivET.text.contains(")"))
        {
            nazivET.setError("Greska u validaciji polja za naziv!")
            isValid=false
        }
        if (nazivET.text.length<2 || nazivET.text.length>40)
        {
            nazivET.setError("Greska u validaciji polja za naziv!")
            isValid=false
        }
        if (porodicaET.text.length<2 || porodicaET.text.length>20)
        {
            porodicaET.setError("Greska u validaciji polja za porodicu!")
            isValid=false
        }
        if (medicinskoUpozorenjeET.text.length<2 || medicinskoUpozorenjeET.text.length>20)
        {
            medicinskoUpozorenjeET.setError("Greska u validaciji polja za upozorenje!")
            isValid=false
        }
        val brojOdabranihKlima=klimatskiTipLV.checkedItemCount
        val brojOdabranihZemlji=zemljisniTipLV.checkedItemCount
        val brojOdabranihKoristi=medicinskaKoristLV.checkedItemCount
        if (brojOdabranihKlima<1)
        {
            showToast("Odaberi bar jednu klimu!")
            isValid=false
        }
        if (brojOdabranihKoristi<1)
        {
            showToast("Odaberi bar jednu medicinsku korist!")
            isValid=false
        }
        if (brojOdabranihZemlji<1)
        {
            showToast("Odaberi bar jednu zemlju!")
            isValid=false
        }
        if (jelaLV.isEmpty())
        {
            jeloET.setError("Niste dodali nijedno jelo!")
            isValid=false
        }
        val brojOdabranihOkusa=profilOkusaLV.checkedItemCount
        if (brojOdabranihOkusa!=1)
        {
            showToast("Nije odabran jedan profil okusa!")
            isValid=false
        }
        return isValid
    }
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private suspend fun saveBiljka(biljka: Biljka){
        biljkaDAO.saveBiljka(biljka)
    }

}