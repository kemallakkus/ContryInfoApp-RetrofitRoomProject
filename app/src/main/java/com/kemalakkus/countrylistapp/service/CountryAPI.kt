package com.kemalakkus.countrylistapp.service

import com.kemalakkus.countrylistapp.model.Country
import io.reactivex.Single
import retrofit2.http.GET

//Bu bir interface. buradan obje oluşturmayız. başka yerlerde implemente ederiz

interface CountryAPI {

    //Burada retrofitte ne işlemler yapacağımızı söyleyecez. GET, POST vs. gibi API bize ne hizmet veriyorsa ona göre işlemler yaparız.
    //Biz bu uygulamada sadece veri çekecez yani GET.
    //verileri değiştirmek istersek POST kullanırız
    //https://raw.githubusercontent.com/atilsamancioglu/IA19-DataSetCountries/master/countrydataset.json
    //BASE_URL -> https://raw.githubusercontent.com/
    //EXT -> atilsamancioglu/IA19-DataSetCountries/master/countrydataset.json

    @GET("atilsamancioglu/IA19-DataSetCountries/master/countrydataset.json")//
    fun getCountries(): Single<List<Country>> //HAngi fonk. içine atanacağını söylüyoruz. Bu fonk. içerisinde nasıl call yapılacağını söylüyoruz. Single kullandık son teknoloji (RxJava yöntemi).

    //Observable devamlı olarak durmayacak şekilde verileri internetten çeker. yani bir veriyi devamlı bir şekilde güncelleyip hızlıca almamız gerekiyorsa observable kullanılır
    //Bir veriyi bir sefer ama garanti bir şekilde almamız gerekiyorsa single kullanılır. Bizim şuanki API miz sürekli güncellenmediği için bunu kullansak sorun olmaz
    //Biz şuan verileri indirip yerel veri tabanımıza kaydetcez. Şöyle bir senaryo olacak. Şu kadar zaman geçtiyse API den tekrar indir. Geçmediyse yerel veri tabanından göstermeye devam et.
    //Yani 19. satırda Observable yazsak ta bir sorun olmayacaktı.



}