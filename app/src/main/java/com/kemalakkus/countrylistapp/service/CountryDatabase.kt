package com.kemalakkus.countrylistapp.service

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.kemalakkus.countrylistapp.model.Country
import kotlinx.coroutines.internal.synchronized

/*Bu veri tabanımızda birden fazla obje oluşturulmasaını istemiyoruz. Bu veri tabanımızdan sadece 1 tane obje oluşturulsun isyiyoruz.
Çünkü eğer farklı zamanlarda ya da aynı zamanda farklı thread lerden bizim veri tabanımıza ulaşılmaya çalışılırsa bu çakuşma oluşturacaktır.
O yüzden biz CountryDatabase'imizi aslında SINGLETON mantığıyla oluşturacaz
 */


@Database(entities = arrayOf(Country::class), version = 1) // Database'i yazınca bizden entityleri arrayOf olarak içine koymamızı istiyor. Bizim 1 adet entity miz old. için içine koyduk. fazla olsaydı virgül koyup onuı da ekleyebilirdik.
abstract class CountryDatabase : RoomDatabase(){

    abstract fun countryDao() : CountryDao // Dökümantasyonda bunu abstract olarak tanımlayıp oluşturduğumuz dao yu döndürmesi gerektiği söyleniyor

    //Singleton -> içerisinden tek bir obje oluşturulabilen sınıftır. Eğer önceden oluşturulmuş obje yoksa oluşturuyoduk.
    //Eğer var ise oluşturmuyoduk. O olan objeyi çekiyoduk.
    // Buna da sadece bir fragment ta ya da main activity de değil app in herhangi bir yerinden ulaşabiliyoduk.
    //Hatta her yerden erişebilmek için companion object i kullanıyoduk

    companion object{

        //Bir CountryDatabase objesi oluşturduk. Null olarak başlıcak.
        // Volatile -> herhangi bir değişkeni volatile tanımladığımız zaman diğer thread lere de görünür hale getirmiş oluruz
        //Yapımızda farklı thread ler coroutine ler kullnamasaydık buna gerek kalmazdı
        //Ama sınıfı singleton yapmamızın amacı da zaten farklı thread lerde çağırma isteğimizdi.

        @Volatile private var instance : CountryDatabase? = null //tek oluşturulacak objemiz bu instance dir. bu obje var mı yok mu onu bi kontrol edecez. ondan sonra işlem yapacaz

        //invoke -> kontrol etme işlemini bi fonk. içinde yapalım dedik. Dökümantasyonda genelde invoke terimi ile yapılır.
        //Invoke ateşlemek başlatamak anlamına gelir. Bizim oluşturduğumuz instance objesinin var olup olmadığını kontrol eder.
        //Sonrasdına eğer yok ise oluşturmak var ise o objeyi döndürmek amacıyla kullanılır.

        private val lock = Any() //synchronized içine bir any tipinde değişken istediği için tanımladık bunu. Synchronized ın kitlenip kitlenmediğini kontrol eden değişken

        operator fun invoke (context: Context) = instance ?: kotlin.synchronized(lock){ //instance var mı yok mu bak. ?: Elvis operatörü.

            //synchronized -> aynı anda sadece tek bir thread de işlem yapılmasına izin verecek. O işlem bittikten sonra başka thread den bu database e gelen olursa işlem yapabilecek.

            //Burda veri tabanımızı oluşturacak fonks.umuzu yazmamız lazım ama şuanlık yok. Aşağıda hemen oluşturcaz bi tane
            instance ?: makeDatabase(context).also {
                instance = it //also dedik yani bunu yap sonra bunu da yap demiş olduk. En son yaptığı instance ı ContryDatabase eeşitle dedik.
            }

        }

        private fun makeDatabase(context: Context) = Room.databaseBuilder( //Database i oluşturuan fonk. yazımı
            context.applicationContext, CountryDatabase::class.java, "countrydatabase"//neden applicationContext çünkü main veya fragment context kullanmak istemedik. yan çevrilir, destroy edilir vs app imizi çökertebilir.
        ).build()

    }


}















