package com.kemalakkus.countrylistapp.service

import com.kemalakkus.countrylistapp.model.Country
import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

//Retrofit objemizi oluşturucaz ki retrofit objemizle beraber işlemlerimizi yapabilelim. Bu yüzden sınıf olmalı
//BASE_URL -> https://raw.githubusercontent.com/
//EXT -> atilsamancioglu/IA19-DataSetCountries/master/countrydataset.json

class CountryAPIService {

    private val BASE_URL = "https://raw.githubusercontent.com/"
    private val api = Retrofit.Builder() //Retrofit kullanarak api mizi oluşturuyoruz
        .baseUrl(BASE_URL) //baseUrl mizi yerine koyuyoruz
        .addConverterFactory(GsonConverterFactory.create()) //json kullanacağımız söylüyoruz. Çünkü modelimizde nasıl serialized yapacağımızı söyledik.
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())//RxJava kullanıcağımızı belirtiyoruz
        .build()
        .create(CountryAPI::class.java) //service içindeki CountryAPI ile birbirine bağlayabiliriz

    fun getData() : Single<List<Country>> { //List içerisinde Country objeleri döndürecek
        return api.getCountries() //döndürmesi için deapi yi çağırcaz nokta koyunca getCountries e ulaşabilmiş olacaz. CountryAPI nin içindeki fonks.

    }



}