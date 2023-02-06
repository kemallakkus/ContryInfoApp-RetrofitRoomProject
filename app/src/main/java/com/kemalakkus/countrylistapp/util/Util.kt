package com.kemalakkus.countrylistapp.util

import android.content.Context
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.kemalakkus.countrylistapp.R

//Extention fonks.u oluşturacaksak eğer direkt hangi sınıfa extend edeceksek hangi sınıfa eklenti yapacaksak onu yazabiliyoruz

/*
fun String.myExtention(myParameter : String){  //örneğin string sınıfına bir extend (eklenti) yazabiliriz. İçine paarametre de verebiliriz

    println(myParameter)

}

 */

//Her oluturulan imageView objesine gllide için fonksiyon tanımlamak istiyoruz

fun ImageView.downloadFromUrl(url : String, progressDrawable: CircularProgressDrawable){ //placeholder içine bi drawable istiyor. Bunun içinde istersek daha iyi olacak.

    val options = RequestOptions() // hata olursa nolacak. burda bi options oluşturup RequestOptions'tan bunları getirebiliriz.
        .placeholder(progressDrawable) // placeholder : görsel inene kadar ne göstericez? Bu önemli çünkü text verileri kadar hızlı gelmez görseller. oraya bir progress bar koymak hoş olur
        //placeholder'ın içi başta boştu. bir drawable istiyodu. aşağıdaki fonks. onu halletti
        .error(R.mipmap.ic_launcher_round) //error olursa ne olacak onu söylücez. R içerisinde mipmap te olan ic_launcher_round(android simgesi) default olarak yüklenecek

    Glide.with(context) //context vermemiz gerekiyor. activity demiyiz fragment temiyiz belli etmek için
        .setDefaultRequestOptions(options) //
        .load(url) //url isticek onu verecez
        .into(this)  //hangi imageView'a referans vermemizi istiyor. ImageView sınıfı içinde old. için this dedik

    //bu kısımda ImageView Extention ımızı oluşturduk

}

fun placeholderProgressBar(context : Context) : CircularProgressDrawable{ //bi fonk. oluşturduk. bize circularProgressBar döndürmesini istedik. fonk.nun içine context vtanımladık çünkü return bi kontext isticek
    return CircularProgressDrawable(context).apply {
        //.apply diyerek özelliklerini yazalım
        strokeWidth = 8f //kalınlık
        centerRadius = 40f //çapı
        start() //CircularProgressBar'ı başlatabiliyoruz

        //burda ImageView kısmında istenen progress drawable oluşturduk.
    }
}

//ARTIK DATABINDING YAPTIĞIMIZ İÇİN FOTOLARI İNDİRMEK İÇİN FONK. LAZIM OLDU

@BindingAdapter("android:downloadUrl") // bu işlem bu fonks. u xml de çalıştırmamıza olanak sağlıyor.
fun downlaodImage(view : ImageView, url: String?){ //xml de fotoğrafları getirmek için fonk. a ihtiyacımız vardı. böyle dandik fonk. ları util de yazdık.

    if (url != null) {
        view.downloadFromUrl(url, placeholderProgressBar(view.context))
    }

}