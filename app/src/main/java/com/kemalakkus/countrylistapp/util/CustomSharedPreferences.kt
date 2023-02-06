package com.kemalakkus.countrylistapp.util

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import androidx.core.content.edit
import kotlinx.coroutines.internal.synchronized

/*yapmak istediğimiz algoritma örneğin 10 dk geçtiğinde direkt api den çek verileri hiç sqlite kullnma. 10dk geçmediyse sqlite tan çek verileri
hiç api kullanma. Bunu yapmanın en iyi yolu ilk olarak ne zaman SQLite a kayıt yapıldysa o zmaanı kaydetmek ve bir dahaki veri çekmede o dakikaya
bakarak kaç dk geçtiğini görmek ve ona göre api ya da sql kullanmaktır.

Bu veri çok küçük bir veri old. için sharedPreferences kullanmak oldukça mantıklı.
SharedPreferences te tek bir obje oluşturulabiliyor ve statik bir objedir. Singleton gibi her yerden çağırabiliriz. O yüzden cfarklı threadlerden
aynı anda çağırılırsa sıkıntıya yol açabilir.

O yüzden biz bunu singleton gibi yapacağız. 2 sebepten ötürü
1- Farklı threadlerden çağırılırsa bir sıkıntıya yol açabilme ihtimali. Tek bir anda senkronize işlem yapılması.
2- Pekiştirmek amaçlı.

 */

class CustomSharedPreferences {

    companion object{

        private val PREFERENCES_TIME = "preferences_time"
        private var sharedPreferences : SharedPreferences? = null

        @Volatile private var instance: CustomSharedPreferences? = null

        private var lock = Any()
        operator fun invoke (context: Context): CustomSharedPreferences = instance ?: kotlin.synchronized(lock){
            instance ?: makeCustomSharedPreferences(context).also {
                instance = it
            }
        }

        private fun makeCustomSharedPreferences(context: Context) : CustomSharedPreferences{
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
            return CustomSharedPreferences()

        }


    }

    fun saveTime(time : Long) { //Bir fonk. daha oluşturaım ki üstteki sharedPreferences objesi ile bu fonks. u kullanabilelim. Zamanı long olarak alalım Çünkü nanotime cinsinden ele alacaz
        sharedPreferences?.edit(commit = true){
            //kotlin extention bize .edit te yeni bişey getirdi. commit i direkt içeride yazabiliyoruz. true veriyoruz ve böylece yapacağımız işlem ne olursa olsun commit true olarak gelecek

            putLong(PREFERENCES_TIME, time)
        }
    }

    fun getTime() = sharedPreferences?.getLong(PREFERENCES_TIME,0) // Zaamnı alabileceğimiz değer

}

//BUNU FeedViewModel İÇİNDE KULLANACAĞIZ