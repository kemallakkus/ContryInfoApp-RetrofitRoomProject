package com.kemalakkus.countrylistapp.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kemalakkus.countrylistapp.R
import com.kemalakkus.countrylistapp.databinding.FragmentCountryBinding
import com.kemalakkus.countrylistapp.util.downloadFromUrl
import com.kemalakkus.countrylistapp.util.placeholderProgressBar
import com.kemalakkus.countrylistapp.viewmodel.CountryViewModel

class CountryFragment : Fragment() {

    private lateinit var viewModel: CountryViewModel // bu fragment'a da bir viewModel tanımlamamız lazım. CountryViewModel olmalı. her fragment'ın kendine özgü viewModel'ı hesabı

    private lateinit var binding: FragmentCountryBinding

    private var countryUuid = 0

    private lateinit var dataBinding: FragmentCountryBinding //databinding te bizim için oluşturulan sınıflardan biri. Databinding i kullanarak return ettiğimiz view i değiştirmek için kullanılacak

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_country, container, false)
        /*binding = FragmentCountryBinding.inflate(layoutInflater,container,false)
        // Inflate the layout for this fragment
        return binding.root */
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {

            countryUuid = CountryFragmentArgs.fromBundle(it).countryUuid

        }

        viewModel = ViewModelProvider(this)[CountryViewModel::class.java] //viewModel'ımızı bu şekilde çağırıyoruz.
        viewModel.getDataFromRoom(countryUuid) //viewModel'ımızı çağırdıktan sonra içindeki fonk.nu bu şekilde çağırabiliyoruz.



        observeLiveData()
    }

    private fun observeLiveData(){ //LiveData'nın işlemlerini alıp buraya aktaracağız
        viewModel.countryLiveData.observe(viewLifecycleOwner, Observer { country ->

            country?.let {

                dataBinding.selectedCountry = country

                /*

                //eşitliğin sol tarafında layout'tak ıd si ile çağırdık .text diyerek içine bişey göndereceğimizi anlattık
                //eşitliğin sağ tarafında Observer içindeki country yardımıyla bize verilen ismi countryName'in içine atmış olduk

                binding.countryName.text = country.countryName
                binding.countryCapital.text = country.countryCapital
                binding.countryCurrency.text = country.countryCurrency
                binding.countryLanguage.text = country.countryLanguage
                binding.countryRegion.text = country.countryRegion
                context?.let {
                    country.imageUrl?.let { it1 -> binding.countryImage.downloadFromUrl(it1, placeholderProgressBar(it)) }
                }

                 */
            }

        })
    }

}







