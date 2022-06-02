package ru.kpfu.itis.carwash.map

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import ru.kpfu.itis.carwash.App
import ru.kpfu.itis.carwash.R
import ru.kpfu.itis.carwash.databinding.FragmentHistoryBinding
import ru.kpfu.itis.carwash.databinding.FragmentSendReviewBinding
import ru.kpfu.itis.carwash.map.model.History
import javax.inject.Inject

class SendReviewFragment : Fragment() {

    private lateinit var binding: FragmentSendReviewBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_send_review, container, false)
        binding.mainToolbar.leftIconClickListener {
            findNavController().popBackStack()
        }
        binding.mainToolbar.textButtonClickListener {
            findNavController().popBackStack()
        }
        return binding.root
    }

}