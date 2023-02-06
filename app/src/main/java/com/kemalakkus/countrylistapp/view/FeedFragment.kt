package com.kemalakkus.countrylistapp.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.kemalakkus.countrylistapp.adapter.CountryAdapter
import com.kemalakkus.countrylistapp.databinding.FragmentFeedBinding
import com.kemalakkus.countrylistapp.model.Country
//import com.kemalakkus.countrylistapp.util.myExtention
import com.kemalakkus.countrylistapp.viewmodel.FeedViewModel


class FeedFragment : Fragment() {

    private lateinit var viewModel: FeedViewModel // ViewModel'ımızı oluşturup o ViewModel'ın datalarını incelemeliyiz. FeedViewModel derken hangi ViewModel'ı nerede kullanıcağımızı ayarlamış olduk

    private val countryAdapter = CountryAdapter(arrayListOf()) //adaptör tanımlayalım. CountryAdapter'i olacak. Bizden country list isticek boş kalsın diye arrayListOf dedik

    private lateinit var binding: FragmentFeedBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFeedBinding.inflate(layoutInflater,container,false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[FeedViewModel::class.java] //viewModel'ı initilazing edicez. Provider hangi fragmenttayız hangi aktivitedeyiz ve hangi viewModel sınıfıyla çalışmak istiyoruz onu belirtebiliyoruz
        //feedfragmentta olduğumuz için ve onunla çalışacağımız için this demek yeterli oldu.
        //çalışmak istediğimiz FeedViewModel old. için onu yazdık ve ::class.java dedik

        viewModel.refreshData() //bunu yaparak FeedViewModel'daki refreshData fonk. nunu çalıştırabiliriz

        binding.countryList.layoutManager = LinearLayoutManager(context) //recyclerView'imizin id si countryList

        //countryList'in layoutManager'ini değişmemiz lazım. LinearLayoutManager olması lazım. Çünkü LinerarLayaoutManager altalta göstermemize olanak sağlayacak.

        binding.countryList.adapter = countryAdapter //adaptörünü belirlememiz lazım. Adaptör buradaki countryAdapter olacak.

        /*
        val myString = "James" //yalandan bir strin tanımladık
        myString.myExtention("hetfield") //oluşturduğumuz eklenti string sınıfına ait bir fonk. gibi çalıştı (util dosyasında tanımlı)


         */


        /*
        binding.button.setOnClickListener {
            val action = FeedFragmentDirections.actionFeedFragmentToCountryFragment()
            Navigation.findNavController(it).navigate(action)
        }
        */

        binding.swipeRefreshLayout.setOnRefreshListener { //kullanıcı aşağı çekip yenileme yapınca ne olacağını belirteceğimiz alan

            binding.countryList.visibility = View.GONE //swipe yapınca kullanıcı recyclerView imiz yani listemiz görünmesin
            binding.countryError.visibility = View.GONE //error da görünmesin
            binding.countryLoading.visibility = View.VISIBLE //kendi yaptığımız progress bar görünsün
            viewModel.refreshFromAPI() //Datayı refresh et diyoruz
            binding.swipeRefreshLayout.isRefreshing = false // android in otomatik gelen progress bar ı kapat



        }

        observeLiveData()
    }

    private fun observeLiveData(){ //buradaki verileri gözlemlediğimizden emin olmamız lazım. bunun için viewModel'ı kullanacaz yine. karışacak diye yeni fonks.da yazdık

        viewModel.countries.observe(viewLifecycleOwner, Observer { countries -> //gözlemleme yöntemi.

            //countries'i gözlemek istedik. LifecycleOwner kimdir? Şuan ki fragmentımızdır. this diyebilirdik. ama bu yöntem daha güzel
            //Observer'ın içi bize List şeklinde Country modeli barındırıyo veriyor. NEden? FeedViewModel'ın içinde countries Mutable olarak öyle tanımlandı aç bak ona.


            countries?.let {

                //.let yaparak countries boş mu dolu mu geliyo denetleyebiliriz.

                binding.countryList.visibility = View.VISIBLE //countries boş değilse countryList'i göster demek. Bu bizim recyclerView'imiz
                countryAdapter.updateCountryList(countries) //Adaptörü çağırıp yeni listeyi koy dedik. O da countries listesi
            }

        })

        viewModel.countryError.observe(viewLifecycleOwner, Observer { error ->
            error?.let {
                if (it) { //if(it) -> bu gösterim boolean true ise demek. Yani burada evet hata var demek
                    binding.countryError.visibility = View.VISIBLE // hata varsa error u gösterr
                } else {
                    binding.countryError.visibility = View.GONE // hata yoksa error u gizle
                }
            }
        })

        viewModel.countryLoading.observe(viewLifecycleOwner, Observer { loading ->
            loading?.let {
                if (it){
                    binding.countryLoading.visibility = View.VISIBLE
                    binding.countryList.visibility = View.GONE
                    binding.countryError.visibility = View.GONE
                }else{
                    binding.countryLoading.visibility = View.GONE
                }
            }

        })

    }
}













