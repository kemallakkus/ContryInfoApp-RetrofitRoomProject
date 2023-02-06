package com.kemalakkus.countrylistapp.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kemalakkus.countrylistapp.model.Country
import com.kemalakkus.countrylistapp.service.CountryAPIService
import com.kemalakkus.countrylistapp.service.CountryDatabase
import com.kemalakkus.countrylistapp.util.CustomSharedPreferences
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch

//Her view'ın (fragment activity ...) kendi viewModel ı olması daha hoştur.

class FeedViewModel(application: Application) : BaseViewModel(application) { //ViewModel olarak tanımlıyoruz

    //LiveData -> canlı veri : obseervable ve observer (gözlemlenebilir ve gözlemci)
    //datalarımızı LiveData olarak tutacaz

    private val countryApiService = CountryAPIService() //API den veri çekebilmemiz için öncelikle servis objemizi oluşturmalıyız
    private val disposable = CompositeDisposable() // Retrofitle internetten veri indirirken her yapılan call aslında hafızada bir yer tutar.
    // Fragmentlar temizlendiğinde bizim bu call lardan kurtulmamız gerekiyor. Yoksa hafızada yer tutuyor.
    //CompositeDİsposible büyük bir obje oluşturuyor. Biz call yaptıkça retrofitle vs. veri indirdikçe bu disposable'ın içine atılıyo. En sonunda disposable'ı kullanıyoruz ve boşaltıyoruz bu call ları

    private var customPreferences = CustomSharedPreferences(getApplication())

    private var refreshTime = 10 * 60 * 1000 * 1000 * 1000L //10 dakikanın nanosaniyeye çevrilmiş hali

    val countries = MutableLiveData<List<Country>>() //ülkeleri alacağımız data. Mutable olması demek değiştirilebilir olması demek. Bu bir liste olacak ve içinde Countryler olacal
    val countryError = MutableLiveData<Boolean>() //hata olduğunda gösterilecek olan. bu ya olacak ya olmayacak o yüzden boolean
    val countryLoading = MutableLiveData<Boolean>() //countrylerin yüklenip yüklenmediğini tutacak

    fun refreshData() { //internetten şuan veri çekmiyorruz. o yüzden dene amaçlı modele veri giriyoruz. Manuel giriyoruz.

        /* Bu kısmı deneme olsun diye manuel yaptık.Bu fonks. içinde verileri otomatik indircez aslında
        val country = Country("Turkey","Asia", "Ankara", "TRY", "Turkish", "www.ss.com")
        val country2 = Country("France","Europe", "Paris", "TRY", "Turkish", "www.ss.com")
        val country3 = Country("Germany","Europe", "Berlin", "TRY", "Turkish", "www.ss.com")

        //3 tane örnek ülke oluşturduk.

        val countryList = arrayListOf<Country>(country,country2,country3) // 3 ülkeyi bir liste içine alıyoruz.

        countries.value = countryList // countries'in değerini countryList'e eşitliyoruz
        countryError.value = false // error false verdik
        countryLoading.value = false // loading false verdik

         */

        //ViewModel'ı YAZDIK AMA NEREDE GÖSTERİLECEĞNİ AYARLAMADIK. MAIN DE Mİ FEED DE Mİ COUNTRY DEMİ NEREDE GÖSTERİLECEK?

        //ŞİMDİ OLUŞTURDUĞUMUZ SERVİSLERİ ViewModel İÇERİSİNDE KULLANICAZ

        val updateTime = customPreferences.getTime() //ne zamana kaydedildiğini artık biliyoruz

        //3 şey kontrol edelim.
        //1- updateTime null mı değil ?
        //2- updateTime sıfıra eşit mi  değil mi
        //3- Güncel olan zaman ile updateTime arasındaki zaman refreshTime dan küçükse Yani 10 dk dan küçükse bu değer
        if (updateTime != null && updateTime != 0L && System.nanoTime() - updateTime < refreshTime){
            getDataFromSQLite()
        }else{
            getDataAPI()

        }


    }

    fun refreshFromAPI() { //her swipe yaptığında api den yüklemesini istedik. O yüzden feedfragment'a gidip 79. satırda refreshData yerine refreshFromAPI fonks. nu verdik
        getDataAPI()
    }

    private fun getDataFromSQLite() { //SQLite'tan verileri çekebilme fonks.u
        countryLoading.value = true
        launch {
            val countries = CountryDatabase(getApplication()).countryDao().getAllCountries() // countries diye liste oluşturalım. Bütün verileri getirelim
            showCountries(countries) //showCountries'i çağırıp içine oluşturduğumuz countries listesini verelim
            Toast.makeText(getApplication(),"Countries From SQLite", Toast.LENGTH_LONG).show()
        }
    }

    //Ama bu veri indirme işlemini yeni bir fonks. içinde yapmak daha mantıklı. Çünkü bi internetten veri çekecez. Daha sonra SQLite'dan da veri çekecez
    //Bunları ayrı ayrı fonkslarda yapmak daha mantıklı olacak

    private fun getDataAPI(){

        countryLoading.value = true

        disposable.add(
            countryApiService.getData() //nereye subscribe olacak nerede gözlemlenecek bunları söylememiz gerekiyor
                .subscribeOn(Schedulers.newThread()) //asenkron bi şekilde yapıcaz - bunu arkaplanda yapıcaz (newThread)
                .observeOn(AndroidSchedulers.mainThread()) //nerede gözlemleyeceğimiz - Gözlemlemeyi kullanıcı arayüzünde yani main thread de yapıcaz (mainThread)
                .subscribeWith(object : DisposableSingleObserver<List<Country>>(){ //observer ımızı söylememiz lazım. Onu da object yapıp object ten türeteceğimiz bişey olacak. Abstract Single observer bu disposable ları asenkron bir şekilde yönetmemize olanak tanıyor.
                    override fun onSuccess(t: List<Country>) { //Single la çalışmanın güzel yanı! Bi hata olursa ya da olmazsa ne yapmamız gerektiğini verebiliyoruz. Observable da bu yok

                        //Verileri çekebilirsek eğer bu verileri gidip SQLite içinde saklamamız gerekiyor. O yüzden buradaki LiveData değişkenlerini kestik buradan.
                        //Çünkü onları ancak SQLite'ta saklama yaptıktan sonra gösterebilriz.

                        storeInSQLite(t)
                        Toast.makeText(getApplication(),"Countries From API", Toast.LENGTH_LONG).show()


                    }

                    override fun onError(e: Throwable) {
                        countryLoading.value = false // yüklenme bittiği için false
                        countryError.value = true //hata var true
                        e.printStackTrace() //Throwbale hata mesajı veriyo
                    }
                })

        )

    }

    //onSuccess altındaki 3 LiveData değişkenini bu fonks. içine aldık. t yerine de countryList i tanımladık mecburen
    private fun showCountries(countryList : List<Country>) {

        countries.value = countryList // LiveData lardan olan bu parametre ülkeleri tutuyo. hata yoksa direkt ver diyoruz. t List<Country> yi veriyo zaten
        countryError.value = false //hata yok bunu gösterme
        countryLoading.value = false //bitti artık o da false

    }

    private fun storeInSQLite(list: List<Country>){ //bir fonk. daha yazıcaz. çünkü aldığımız verileri sql e kaydetmek istiyoruz.
        //bunun içinde artık coroutine'leri kullanabiliriz. Coroutine'ler therading yapmamıza olabak tanıyordu.
        //Ancak Coroutine i kullanmak için bazı ayarlamalar yapmak lazım. arka planda mı main thread de mi çalışacak belirtmemiz lazım.
        //Bu gibi ayarlamaları yeni bir sınıf içinde yapıp en başta FeedViewModel ımızı ViewModel yerine bu yeni sınıfı yazarak referans alacaz. Böylece coroutine i ne için nasıl kullanacağımızı burada belirtmiş olacağız
        //Peki ViewModel a extend etmiştik o ne olacak. Yeni sınıfımızı(BaseViewModel) ViewModel'a extend ederiz o zmaan. ve içerisinde coroutine leri de kullanırız
        //Böylece hem ViewModel hem coroutine olan bütün sınıflarımızda bunu extend ederek kullanabiliriz.
        //Bizim şuan 2 ViewModel ımız var ama 20 tane old. durumlar da olabilir. O yüzden bu mantıklı bir yol

        launch {
            //Burada artık database'imizle ilgili işlemlerimizi yapabiliriz.

            val dao = CountryDatabase(getApplication()).countryDao() //database'imizi obje olarak tanımlayalım. Dao yu bir değişken olarak oluşturduk burada
            dao.deleteAllCountries() //önceden kalan tüm verileri silelim öncelikle. dao. yazarak suspend fonk.larımıza erişim sağlayabildik
            val listLong = dao.insertAll(*list.toTypedArray()) //toTypedArray -> Listeyi tekil elemanlaara dönüştürür. Ekleme yaptık. Bize verilen Country listesindeki herşeyi tek tek ekledik.Bu bize uuid listesi döndürdü(listLong)
            var i = 0
            while (i<list.size){ //listemiz içerisinde kaç tane eleman var. Diyelim ki 10 eleman var bu döngü 10 defa çalışacak
                list[i].uuid = listLong[i].toInt() // döndürdüğü listeyi tek tek bizim kendilistemiz içindeki elemanlara atadı
                i = i+1
            }

            showCountries(list) //en sonunda ülkeleri göster diyoruz.
        //TÜM BUNLARI onSuccess İÇİNDE YANİ API DEN ALMA BAŞARILIYSA YAPIYORUZ.
        }

        customPreferences.saveTime(System.nanoTime()) //nanotime alabileceğimiz en detaylı zamanı bize vermiş oluyor. Artık zamanı kaydediyoruz.

    }

    override fun onCleared() { //hafızayı etkin kullanmak için. fragmentlar yok olunca burdan kolaylıkla kurtulabiliriz. CompositeDisposable a bak en üstte
        super.onCleared()

        disposable.clear()
    }

}

















