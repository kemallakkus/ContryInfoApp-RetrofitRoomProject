package com.kemalakkus.countrylistapp.adapter

import android.view.View

interface CountryClickListener {

    fun onCountryClicked(v: View) //country tıklanıldığında ne olacak fonksiyonu. Bi tane view versin bize tıkladığımız view neyse onu bize geri döndürsün

    //sadece bunu yaptıktan sonra geri kalanını xml içinde halledecez

}