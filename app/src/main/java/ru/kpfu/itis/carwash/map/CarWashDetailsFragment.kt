package ru.kpfu.itis.carwash.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import ru.kpfu.itis.carwash.R
import ru.kpfu.itis.carwash.databinding.FragmentCarWashDetailsBinding
import ru.kpfu.itis.carwash.map.model.CarWash

class CarWashDetailsFragment(val carWash: CarWash) : Fragment() {

    private lateinit var binding: FragmentCarWashDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_car_wash_details, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.location.text = carWash.address
        binding.phone.text = carWash.phone
    }
}