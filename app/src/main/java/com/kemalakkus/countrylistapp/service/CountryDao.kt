package com.kemalakkus.countrylistapp.service

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.kemalakkus.countrylistapp.model.Country

//dökümantasyonda 3 ana öğe olduğu görülüyor. 1 entity modelde yazdık. 2 dao onu burada yazdık. 3. olarak veri tabanının kendisini yazacaz

@Dao

interface CountryDao {

    //Data Access Object
    //Burada veri tabanımıza ulaşmak için kullanacağımız yöntemleri yazıyoruz.

    @Insert

    suspend fun insertAll(vararg countries: Country): List<Long>

    //Insert -> INSERT INTO gibi database'e ulaşım methodumuz.
    //suspend -> coroutine'ler içerisinde çağırılıyor ve fonksiyonları durdurulup devam ettirilmeye olanak veren keyword'ümüz
    /*vararg ->SQLite'ta insert yaparken tek tek objeleri insert ediyoduk. Bu objeler burada country objeleri şuan.
        Fakat biz şuan sadece 1 tane değil istediğimiz zaman istediğimiz kadar bu objeyi vermek istiyoruz. vararg yerine var yazıp tek bir tane alsak
        olmaz. Liste de olmaz çünkü liste olarak değil tek tek verecez. 10 tane 100 tane verecem desem o da olmaz çünkü netten çekiyoruz verileri
        ne kadar veri var bilmiyoruz.
        İŞTE vararg burada işe yarıyor. Sayısını bilmediğimiz zamanlarda bir tekil objeyi farklı sayılarda verebilmemiz için gerekli olan keyword'ümüzdür.
     */
    //Herşeyin sonunda bize bir Liste dödürecek. ve içinde döndürdüğü Long listesi aslında primary id'leri döndürecek

    //vararg -> Multiple Country Object
    //List<Long> -> primary keys

    @Query("SELECT * FROM country") //Entity de ayzan sınıf ismimiz country old. için aynısını yazdık. Bu hepsini getirecek
    suspend fun getAllCountries() : List<Country> //bu bütün country leri getir bana ve bir country listesi ver fonks.u olsun

    @Query("SELECT * FROM country WHERE uuid = :countryId") //Modelde primaryKey'de uuid diye bir değişken tanımladık. Burada filtre şlemi yaptık. uuid si countryId'ye eşit olanları sadece çek diyebiliyoruz.
    suspend fun getCountry(countryId : Int) : Country //bu da bize tek bir country döndürsün. Bunu Country Fragment ta kullanacaz. Ülkenin ayrıntılarının olduğu sayfa. İçinde bir id istesek güzel olur.

    @Query("DELETE FROM country") //Bütün veri tabanını silebiliriz.
    suspend fun deleteAllCountries()

}










