package ru.kpfu.itis.carwash.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import ru.kpfu.itis.carwash.R
import ru.kpfu.itis.carwash.databinding.FragmentReviewsBinding
import ru.kpfu.itis.carwash.map.model.Review

class ReviewsFragment : Fragment() {

    private lateinit var binding: FragmentReviewsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_reviews, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val reviews = listOf(
            Review(
                fullName = "Иван Жестков",
                review = "Пользовался услугами этой мойки  3 года, но к сожалению в последнее время , качество обслуживания упало.",
                rating = 3,
                time = "год назад",
            ),
            Review(
                fullName = "Артур Валеев",
                review = "Цена-качество отличное, несколько раз там мылся, и могу точно сказать что за свои деньги мойка хорошая",
                rating = 4,
                time = "3 года назад",
            ),
        )
        binding.rv.adapter = ReviewsAdapter(reviews)
        binding.btn.setOnClickListener {
            findNavController().navigate(R.id.action_bottomSheetFragment_to_sendReviewFragment)
        }
    }
}