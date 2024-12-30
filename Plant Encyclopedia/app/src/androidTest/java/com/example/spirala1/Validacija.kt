package com.example.spirala1

import ToastMatcher
import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.clearText
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.matcher.ViewMatchers.hasErrorText
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withText
import org.hamcrest.Matchers.instanceOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class Validacija { //click() mi ne radi iz nekog razloga***************************************************
    @get:Rule
    var activityRule: ActivityScenarioRule<MainActivity> = ActivityScenarioRule(MainActivity::class.java)
    @Test
    fun validacijaNaziva()
    {
        val intent=Intent(ApplicationProvider.getApplicationContext(),NovaBiljkaActivity::class.java)
        launchActivity<NovaBiljkaActivity>(intent)
        onView(withId(R.id.nazivET)).perform(typeText("x"),closeSoftKeyboard())
        onView(withId(R.id.dodajBiljkuBtn)).perform(click())
        onView(withId(R.id.nazivET)).check(matches(hasErrorText("Greska u validaciji polja za naziv!")))
        onView(withId(R.id.nazivET)).perform(clearText())
        onView(withId(R.id.nazivET)).perform(typeText("ova recenica je zaista preduga"),closeSoftKeyboard())
        onView(withId(R.id.dodajBiljkuBtn)).perform(click())
        onView(withId(R.id.nazivET)).check(matches(hasErrorText("Greska u validaciji polja za naziv!")))
    }
    @Test
    fun validacijaPorodice()
    {
        val intent=Intent(ApplicationProvider.getApplicationContext(),NovaBiljkaActivity::class.java)
        launchActivity<NovaBiljkaActivity>(intent)
        onView(withId(R.id.porodicaET)).perform(typeText("x"),closeSoftKeyboard())
        onView(withId(R.id.dodajBiljkuBtn)).perform(click())
        onView(withId(R.id.porodicaET)).check(matches(hasErrorText("Greska u validaciji polja za porodicu!")))
        onView(withId(R.id.porodicaET)).perform(clearText())
        onView(withId(R.id.porodicaET)).perform(typeText("ova recenica je zaista preduga"),closeSoftKeyboard())
        onView(withId(R.id.dodajBiljkuBtn)).perform(click())
        onView(withId(R.id.porodicaET)).check(matches(hasErrorText("Greska u validaciji polja za porodicu!")))
    }
    @Test
    fun validacijaMedicinskogUpozorenja()
    {
        val intent=Intent(ApplicationProvider.getApplicationContext(),NovaBiljkaActivity::class.java)
        launchActivity<NovaBiljkaActivity>(intent)
        onView(withId(R.id.medicinskoUpozorenjeET)).perform(typeText("x"),closeSoftKeyboard())
        onView(withId(R.id.dodajBiljkuBtn)).perform(click())
        onView(withId(R.id.medicinskoUpozorenjeET)).check(matches(hasErrorText("Greska u validaciji polja za upozorenje!")))
        onView(withId(R.id.medicinskoUpozorenjeET)).perform(clearText())
        onView(withId(R.id.medicinskoUpozorenjeET)).perform(typeText("ova recenica je zaista preduga"),closeSoftKeyboard())
        onView(withId(R.id.dodajBiljkuBtn)).perform(click())
        onView(withId(R.id.medicinskoUpozorenjeET)).check(matches(hasErrorText("Greska u validaciji polja za upozorenje!")))
    }
    @Test
    fun validacijaJela()
    {
        val intent=Intent(ApplicationProvider.getApplicationContext(),NovaBiljkaActivity::class.java)
        launchActivity<NovaBiljkaActivity>(intent)
        onView(withId(R.id.jeloET)).perform(typeText("x"),closeSoftKeyboard())
        onView(withId(R.id.dodajJeloBtn)).perform(click())
        onView(withId(R.id.jeloET)).check(matches(hasErrorText("Netacan unos jela")))
        onView(withId(R.id.jeloET)).perform(clearText())
        onView(withId(R.id.jeloET)).perform(typeText("ova recenica je zaista preduga"),closeSoftKeyboard())
        onView(withId(R.id.dodajJeloBtn)).perform(click())
        onView(withId(R.id.jeloET)).check(matches(hasErrorText("Netacan unos jela")))
    }
    @Test
    fun validacijaJelaDuplikat()
    {
        val intent=Intent(ApplicationProvider.getApplicationContext(),NovaBiljkaActivity::class.java)
        launchActivity<NovaBiljkaActivity>(intent)
        onView(withId(R.id.jeloET)).perform(typeText("grah"),closeSoftKeyboard())
        onView(withId(R.id.dodajJeloBtn)).perform(click())
        onView(withId(R.id.medicinskoUpozorenjeET)).perform(clearText())
        onView(withId(R.id.jeloET)).perform(typeText("GRah"),closeSoftKeyboard())
        onView(withId(R.id.dodajJeloBtn)).perform(click())
        onView(withId(R.id.medicinskoUpozorenjeET)).check(matches(hasErrorText("Jelo vec postoji")))
    }
    @Test
    fun validacijaJeloPrazno()
    {
        val intent=Intent(ApplicationProvider.getApplicationContext(),NovaBiljkaActivity::class.java)
        launchActivity<NovaBiljkaActivity>(intent)
        onView(withId(R.id.dodajBiljkuBtn)).perform(click())
        onView(withId(R.id.jeloET)).check(matches(hasErrorText("Niste dodali nijedno jelo!")))
    }
    @Test
    fun nijednaKlima() {
        val intent = Intent(ApplicationProvider.getApplicationContext(), NovaBiljkaActivity::class.java)
        launchActivity<NovaBiljkaActivity>(intent)
        onView(withId(R.id.dodajBiljkuBtn)).perform(click())
        onView(withText("Odaberi bar jednu klimu!")).inRoot(ToastMatcher()).check(matches(withText("Odaberi bar jednu klimu!")))
    }
    @Test
    fun nijednaZemlja()
    {
        val intent = Intent(ApplicationProvider.getApplicationContext(), NovaBiljkaActivity::class.java)
        launchActivity<NovaBiljkaActivity>(intent)
        onView(withId(R.id.dodajBiljkuBtn)).perform(click())
        onView(withText("Odaberi bar jednu zemlju!")).inRoot(ToastMatcher()).check(matches(withText("Odaberi bar jednu zemlju!")))
    }
    @Test
    fun nijednaKorist()
    {
        val intent = Intent(ApplicationProvider.getApplicationContext(), NovaBiljkaActivity::class.java)
        launchActivity<NovaBiljkaActivity>(intent)
        onView(withId(R.id.dodajBiljkuBtn)).perform(click())
        onView(withText("Odaberi bar jednu medicinsku korist!")).inRoot(ToastMatcher()).check(matches(withText("Odaberi bar jednu medicinsku korist!")))
    }
    @Test
    fun nijedanOkus()
    {
        val intent = Intent(ApplicationProvider.getApplicationContext(), NovaBiljkaActivity::class.java)
        launchActivity<NovaBiljkaActivity>(intent)
        onView(withId(R.id.dodajBiljkuBtn)).perform(click())
        onView(withText("Nije odabran jedan profil okusa!")).inRoot(ToastMatcher()).check(matches(withText("Nije odabran jedan profil okusa!")))
    }
}