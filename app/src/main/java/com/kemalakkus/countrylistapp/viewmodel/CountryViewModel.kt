package com.kemalakkus.countrylistapp.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kemalakkus.countrylistapp.model.Country
import com.kemalakkus.countrylistapp.service.CountryDatabase
import kotlinx.coroutines.launch

class CountryViewModel(application: Application) : BaseViewModel(application) { //ViewModel olarak oluşturduk
    val countryLiveData = MutableLiveData<Country>() // bir tane datamız olacak. Çünkü burada sadeec seçilen ülkenin bilgileri olacak sadece. Tipi Country olacak

    fun getDataFromRoom(uuid : Int){ //Room database de kullanacaz. internetten çektiğimiz verileri kendi lokal veri tabanımıza da kaydetcez.

        /*burada manuel veri oluşturduk. CountryFragment ımızda bişeyler görünsün diye sadece

        val country = Country("Turkey","Asia","Ankara","TRY","Turkish","www.ss.com")
        //denemelik bi country verisi oluşturduk elimizle

        countryLiveData.value = country //böyle yaparak countryLiveData'ya coubtry datasını vermiş olduk

         */

        launch {
            //coroutine de çalışıcaz. scope umuzu açtık
            val dao = CountryDatabase(getApplication()).countryDao() // dao muzu oluşturduk
            val country = dao.getCountry(uuid) // vir adet ülke seçicez. country değişkenine atanacak.
            countryLiveData.value = country //onu da alıp countryLiveData mıza atacaz

        }
    }

}
//BUNLARI YAPTIK DA SONRA GİDİP CountryFragment ımıza UYGULAYACAZ