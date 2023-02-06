package com.kemalakkus.countrylistapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.kemalakkus.countrylistapp.R
import com.kemalakkus.countrylistapp.databinding.ItemCountryBinding
import com.kemalakkus.countrylistapp.model.Country
import com.kemalakkus.countrylistapp.util.downloadFromUrl
import com.kemalakkus.countrylistapp.util.placeholderProgressBar
import com.kemalakkus.countrylistapp.view.FeedFragmentDirections

//Adapter'de ne yapıcaz. Dışarıdan bir arrayList isticez. MEsela FeedFragment içerisinde kullanıcaksak FeedFragment için verielri viewModel'dan çekcez
// viewModel'dan çekeceğimiz verileri bu adaptöre iletmemiz gerekecek. O yüzden constructor'da bir array list tanımlayacaz "countryList"
//Bu arrayList te içerisinde Country diye adlandırdığımız modeli barındıracak.

class CountryAdapter(val countryList: ArrayList<Country>):RecyclerView.Adapter<CountryAdapter.CountryViewHolder>(), CountryClickListener {
    //en sonda CountryClickListener ı implement ettik çünkü artık databinding yapıyoruz.

    class CountryViewHolder(var view : ItemCountryBinding) : RecyclerView.ViewHolder(view.root){

        //artık databinding yapıyoruz o yüzden diğerini yoruma aldım. bazı değişiklikler yapmamız gerekti
        //ilk olarak viewHolder da view : ItemCountryBinding yaptım. Bu sefer RecyclerView.ViewHolder içinde ki view e . koyarak root u ekledim

    }

    //ViewHolder istiyor yukarıdaki implementation. O yüzden burada bir vievHolder sınıfı oluşturuyoruz.

    /*class CountryViewHolder(var view : View): RecyclerView.ViewHolder(view) {

    //CountryViewHolder'ı gerçekten bir ViewHolder olarak tanımlamamızı istiyor. Constructor bizden bir view istiyor.
        //alt enter yapınca da memberları ekliyoruz ve meşhur 3 fonsiyon geliyor.
    }*/

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryViewHolder {

        //Bu fonks. bizden CountryViewHolder döndürmemizi bekliyor.
        //Burada layout ile yani item_country.xml ile bu adaptörü birbirine bağlıyoruz
        // Bu bağlama işi için inflater kullanıyoruz

        val inflater = LayoutInflater.from(parent.context) //inlate'imiz Layout inflate ti. context zaten parent olarak veriliyor.

        //val view = inflater.inflate(R.layout.item_country,parent,false) // oluşturduğumuz inflater sayesinde bir view oluşturuyoruz.
        //bize hangi layout'u bağlamak istediğimizi soruyor. R.layout. diyerek istediğimizi yazıyoruz

        val view = DataBindingUtil.inflate<ItemCountryBinding>(inflater,R.layout.item_country,parent,false)

        //artık databinding yaptığımız için view imizi yeniden tanımlamamız gerekti
        //inflate i burda çağırıcaz

        return CountryViewHolder(view) //Son olarak CountryViewHolder döndür diycez. view'ı parametre olarak buraya verecez.

        //Bu parametre CountryViewHolder sınıfındaki constructor'a girecek. O da bu parametreyi REcyclerView.ViewHolder içine paslayacak ve bağlama sağlanacak

    }

    override fun getItemCount(): Int { //kaç tane row oluşturacağımızı soruyor. countryList kadar oluşturması için .size diyoruz
        return countryList.size
    }

    override fun onBindViewHolder(holder: CountryViewHolder, position: Int) {

        //ARTIK DATABINDING KULLANIYORUZ. ALTTAKİ BİR SÜRÜ HOLDER YERİNE LAYOUT TA TANIMLADIĞIMIZ VARIABLE I BURADA ÇAĞIRIYORUZ
        //TEK TEK NAME DİR REGİON DUR ÇAĞIRMICAZ
        //DİREKT COUNTRY İ ÇAĞIRDIK. DİREKT BU COUNTRY E ERİŞİP  COUNTRYLIST TEN GÜNCEL POSITION  NEYSE ONU GETİR DEMEM YETERLİ


        holder.view.country = countryList[position]
        holder.view.listener = this

        //DATABINDING YAPTIĞIMIZDA IMAGE I BURDA ARTIK ÇAĞIRMIYORUZ. UTIL DE Bİ FONK. YAZDIK ONU DA GİDİP XML DE ÇAĞIRDIK HALLOLDU
        //SADECE setOnClickListener KALDI. O BİRAZ KARIŞIK. KENDİMİZ Bİ ARAYÜZ OLUŞTURMAK ZORUNDA KALACAĞIZ BU YÜZDEN.
        //ADAPTER LA İLGİLİ OLD. İÇİN ADAPTER İÇİNDE BİR INTERFACE OLUŞTURACAZ


        /*

        //kullandığımız layouttaki yani item_country deki imageView, text view gibi parçalara ulaşabiliyoruz.

        holder.view.findViewById<TextView>(R.id.name).text = countryList[position].countryName

        //name'e ulaştık. ilgili ülke neyse onun ismini alacak. onu da bize countryList içinde hangi pozisyondaysak onu çağıracak. nokta deyince
        //country modelimizdeki özellikler çıkıyor. name'e karşılık geleni seçiyoruz

        holder.view.findViewById<TextView>(R.id.region).text = countryList[position].countryRegion

        holder.view.setOnClickListener {
            //bir yere tıklandığında ne yapılması gerektiğini veren fonksiyon

            val action = FeedFragmentDirections.actionFeedFragmentToCountryFragment(countryList[position].uuid) //aksiyonumuz FeedFragmentDirection.action'dan gelecek
            //action.countryUuid

            Navigation.findNavController(it).navigate(action)
        }

        countryList[position].imageUrl?.let {
            holder.view.findViewById<ImageView>(R.id.imageView).downloadFromUrl( //imageView'ımızı gösterebiliriz artık.
                it,
                placeholderProgressBar(holder.view.context))
        }


         */

    }

    fun updateCountryList(newCountryList: List<Country>) { //yeni bir countrylist isticez. Liste içinde Country leri

        //swipeRefresh yaptığımızda bunu adaptörümüze de bildirmemiz lazım. Bu fonks. onun için.
        //Sonuç olarak bu fonks. çağğırıldığında bir liste istenecek bizden. Biz de oluşturuduğumuz bu yeni listeyi verecez.

        countryList.clear() //öncelikle countryList i silecez
        countryList.addAll(newCountryList) // countryList ile newCountryList i değişmiş olucaz
        notifyDataSetChanged() // Adaptörü yenilemek için kullanılan metot
    }

    override fun onCountryClicked(v: View) { //CountryClickListener interface ini implement ettikten sonra member ları uyguladık bu fonks. geldi

        //BU ACTION BU UUID İ ALIP DİĞER TARAFA PASLAYACAK

        val uuid = v.findViewById<TextView>(R.id.countryUuidText).text.toString().toInt()
        val action = FeedFragmentDirections.actionFeedFragmentToCountryFragment(uuid) //aksiyonumuz FeedFragmentDirection.action'dan gelecek
        //action.countryUuid

        Navigation.findNavController(v).navigate(action)
    }

}


