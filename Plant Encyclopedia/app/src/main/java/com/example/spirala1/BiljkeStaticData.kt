package com.example.spirala1

private val biljke=mutableListOf(
    Biljka(
        naziv = "Bosiljak (Ocimum basilicum)",
        porodica = "Lamiaceae (usnate)",
        medicinskoUpozorenje = "Može iritirti kožu osjetljivu na sunce. Preporučuje se oprezna upotreba pri korištenju ulja bosiljka.",
        medicinskeKoristi = listOf(MedicinskaKorist.SMIRENJE, MedicinskaKorist.REGULACIJAPROBAVE),
        profilOkusa = ProfilOkusaBiljke.BEZUKUSNO,
        jela = listOf("Salata od paradajza", "Punjene tikvice"),
        klimatskiTipovi = listOf(KlimatskiTip.SREDOZEMNA, KlimatskiTip.SUBTROPSKA),
        zemljisniTipovi = listOf(Zemljište.PJESKOVITO, Zemljište.ILOVACA)),
    Biljka(
        naziv = "Nana (Mentha spicata)",
        porodica = "Lamiaceae (metvice)",
        medicinskoUpozorenje = "Nije preporučljivo za trudnice, dojilje i djecu mlađu od 3 godine.",
        medicinskeKoristi = listOf(MedicinskaKorist.PROTUUPALNO, MedicinskaKorist.PROTIVBOLOVA),
        profilOkusa = ProfilOkusaBiljke.MENTA,
        jela = listOf("Jogurt sa voćem", "Gulaš"),
        klimatskiTipovi = listOf(KlimatskiTip.SREDOZEMNA, KlimatskiTip.UMJERENA),
        zemljisniTipovi = listOf(Zemljište.GLINENO, Zemljište.CRNICA)
    ),
    Biljka(
        naziv = "Kamilica (Matricaria chamomilla)",
        porodica = "Asteraceae (glavočike)",
        medicinskoUpozorenje = "Može uzrokovati alergijske reakcije kod osjetljivih osoba.",
        medicinskeKoristi = listOf(MedicinskaKorist.SMIRENJE, MedicinskaKorist.PROTUUPALNO),
        profilOkusa = ProfilOkusaBiljke.AROMATICNO,
        jela = listOf("Čaj od kamilice"),
        klimatskiTipovi = listOf(KlimatskiTip.UMJERENA, KlimatskiTip.SUBTROPSKA),
        zemljisniTipovi = listOf(Zemljište.PJESKOVITO, Zemljište.KRECNJACKO)
    ),
    Biljka(
        naziv = "Ružmarin (Rosmarinus officinalis)",
        porodica = "Lamiaceae (metvice)",
        medicinskoUpozorenje = "Treba ga koristiti umjereno i konsultovati se sa ljekarom pri dugotrajnoj upotrebi ili upotrebi u većim količinama.",
        medicinskeKoristi = listOf(MedicinskaKorist.PROTUUPALNO, MedicinskaKorist.REGULACIJAPRITISKA),
        profilOkusa = ProfilOkusaBiljke.AROMATICNO,
        jela = listOf("Pečeno pile", "Grah","Gulaš"),
        klimatskiTipovi = listOf(KlimatskiTip.SREDOZEMNA, KlimatskiTip.SUHA),
        zemljisniTipovi = listOf(Zemljište.SLJUNKOVITO, Zemljište.KRECNJACKO)
    ),
    Biljka(
        naziv = "Lavanda (Lavandula angustifolia)",
        porodica = "Lamiaceae (metvice)",
        medicinskoUpozorenje = "Nije preporučljivo za trudnice, dojilje i djecu mlađu od 3 godine. Također, treba izbjegavati kontakt lavanda ulja sa očima.",
        medicinskeKoristi = listOf(MedicinskaKorist.SMIRENJE, MedicinskaKorist.PODRSKAIMUNITETU),
        profilOkusa = ProfilOkusaBiljke.AROMATICNO,
        jela = listOf("Jogurt sa voćem"),
        klimatskiTipovi = listOf(KlimatskiTip.SREDOZEMNA, KlimatskiTip.SUHA),
        zemljisniTipovi = listOf(Zemljište.PJESKOVITO, Zemljište.KRECNJACKO)
    ), //Sada dodajem dole 5 svojih
    Biljka(
        naziv = "Šumska jagoda (Fragaria vesca)",
        porodica = "Rosaceae (ružovke)",
        medicinskoUpozorenje = "",
        medicinskeKoristi = listOf(MedicinskaKorist.REGULACIJAPROBAVE, MedicinskaKorist.PODRSKAIMUNITETU),
        profilOkusa = ProfilOkusaBiljke.SLATKI,
        jela = listOf("Jogurt sa voćem","Puding"),
        klimatskiTipovi = listOf(KlimatskiTip.PLANINSKA),
        zemljisniTipovi = listOf(Zemljište.CRNICA)
    ),
    Biljka(
        naziv = "Mango (Mangifera indica)",
        porodica = "Anacardiaceae (rujevke)",
        medicinskoUpozorenje = "Prekomjerno konzumiranje može dovesti do mučnine.",
        medicinskeKoristi = listOf(MedicinskaKorist.REGULACIJAPRITISKA, MedicinskaKorist.PODRSKAIMUNITETU),
        profilOkusa = ProfilOkusaBiljke.CITRUSNI,
        jela = listOf("Jogurt sa voćem","Pavlova torta"),
        klimatskiTipovi = listOf(KlimatskiTip.TROPSKA, KlimatskiTip.SREDOZEMNA),
        zemljisniTipovi = listOf(Zemljište.PJESKOVITO, Zemljište.SLJUNKOVITO, Zemljište.SLJUNKOVITO)
    ),
    Biljka(
        naziv = "Gavez (Symphytum officinale)",
        porodica = "Boraginaceae (boražinovke)",
        medicinskoUpozorenje = "Ne preporučuje se konzumiranje cvijeta biljke.",
        medicinskeKoristi = listOf(MedicinskaKorist.PROTIVBOLOVA, MedicinskaKorist.PROTUUPALNO, MedicinskaKorist.PODRSKAIMUNITETU, MedicinskaKorist.REGULACIJAPROBAVE),
        profilOkusa = ProfilOkusaBiljke.KORIJENASTO,
        jela = emptyList(),
        klimatskiTipovi = listOf(KlimatskiTip.PLANINSKA, KlimatskiTip.UMJERENA),
        zemljisniTipovi = listOf(Zemljište.GLINENO, Zemljište.ILOVACA)
    ),
    Biljka(
        naziv = "Habanero paprika (Capsicum chinense)",
        porodica = "Solanaceae (pomoćnice)",
        medicinskoUpozorenje = "Izuzetno ljuta paprika uzrokuje stomačne tegobe pri pretjeranoj upotrebi.",
        medicinskeKoristi = listOf(MedicinskaKorist.PODRSKAIMUNITETU, MedicinskaKorist.REGULACIJAPRITISKA),
        profilOkusa = ProfilOkusaBiljke.LJUTO,
        jela = listOf("Gulaš","Paprikaš","Ljuti sos","Grah","Punjene paprike"),
        klimatskiTipovi = listOf(KlimatskiTip.UMJERENA, KlimatskiTip.SREDOZEMNA, KlimatskiTip.SUBTROPSKA),
        zemljisniTipovi = listOf(Zemljište.SLJUNKOVITO, Zemljište.KRECNJACKO, Zemljište.PJESKOVITO)
    ),
    Biljka(
        naziv = "Maslačak (Taraxacum)",
        porodica = "Asteraceae (glavočike)",
        medicinskoUpozorenje = "Paziti da porijeklo biljke dolazi sa provjerenog zemljišta",
        medicinskeKoristi = listOf(MedicinskaKorist.REGULACIJAPROBAVE, MedicinskaKorist.PODRSKAIMUNITETU, MedicinskaKorist.SMIRENJE),
        profilOkusa = ProfilOkusaBiljke.GORKO,
        jela = listOf("Čaj od suhog korijena","Sok od svježeg korijena"),
        klimatskiTipovi = listOf(KlimatskiTip.UMJERENA, KlimatskiTip.SUHA, KlimatskiTip.PLANINSKA),
        zemljisniTipovi = listOf(Zemljište.ILOVACA, Zemljište.CRNICA)
    ))
private val newBiljke= mutableListOf<Biljka>()

fun getBiljke(): MutableList<Biljka>{
    return (biljke+ newBiljke) as MutableList<Biljka>
}

fun dodavanjeNoveBiljke(biljka: Biljka)
{
    newBiljke.add(biljka)
}

fun getKlime(): List<String>{
    val klime = listOf<String>(
        KlimatskiTip.SREDOZEMNA.opis,
        KlimatskiTip.TROPSKA.opis,
        KlimatskiTip.SUBTROPSKA.opis,
        KlimatskiTip.UMJERENA.opis,
        KlimatskiTip.SUHA.opis,
        KlimatskiTip.PLANINSKA.opis
    )
    return klime
}

fun getBoje(): List<String>{
    val boje = listOf<String>("red","blue","yellow","orange","purple","brown","green")
    return boje
}

fun getKoristi(): List<String>{
    val koristi = listOf<String>(
        MedicinskaKorist.SMIRENJE.opis,
        MedicinskaKorist.PROTUUPALNO.opis,
        MedicinskaKorist.PROTIVBOLOVA.opis,
        MedicinskaKorist.REGULACIJAPRITISKA.opis,
        MedicinskaKorist.REGULACIJAPROBAVE.opis,
        MedicinskaKorist.PODRSKAIMUNITETU.opis,
    )
    return koristi
}

fun getZemlja(): List<String>{
    val zemlje = listOf<String>(
        Zemljište.PJESKOVITO.naziv,
        Zemljište.GLINENO.naziv,
        Zemljište.ILOVACA.naziv,
        Zemljište.CRNICA.naziv,
        Zemljište.SLJUNKOVITO.naziv,
        Zemljište.KRECNJACKO.naziv
    )
    return zemlje
}

fun getOkusi(): List<String>{
    val okusi = listOf<String>(
        ProfilOkusaBiljke.MENTA.opis,
        ProfilOkusaBiljke.CITRUSNI.opis,
        ProfilOkusaBiljke.SLATKI.opis,
        ProfilOkusaBiljke.BEZUKUSNO.opis,
        ProfilOkusaBiljke.LJUTO.opis,
        ProfilOkusaBiljke.KORIJENASTO.opis,
        ProfilOkusaBiljke.AROMATICNO.opis,
        ProfilOkusaBiljke.GORKO.opis
    )
    return okusi
}