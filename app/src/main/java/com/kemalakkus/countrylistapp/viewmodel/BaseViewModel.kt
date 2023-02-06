package com.kemalakkus.countrylistapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

//AndroidViewModel dedik daha güvenli bir sınıf olsun diye
//abstract çünkü obje oluşturmucaz sadece extend edilmesi için kullanacaz

abstract class BaseViewModel(application: Application) : AndroidViewModel(application), CoroutineScope  {

    //abstract val list: Any

    //abstract var list: Any
    private val job = Job()  //coroutine'imizin ne iş yapacağını söylememiz lazım. Bu aslında bir iş oluşturuyor. Ve bu iş aslıunda arka planda yapılacak olan iş
    
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main //Bu şu demek; Öncelikle arkaplanda işini yap (job) sonra main thread e dön.

    override fun onCleared() { //eğer ki app bir şekilde destroy olursa örneğin app kapatılıt-rsa 
        super.onCleared()
        job.cancel() //iş iptal edilecektir.
    }
}

//MODELİMİZİ YAZDIK. ARTIK FeedViewModel ve CountryViewModel'da EXTEND İÇİN ViewModel YERİNE BaseViewModel'ı KULLANACAZ