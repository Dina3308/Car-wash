package ru.kpfu.itis.carwash.map

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import ru.kpfu.itis.carwash.R
import ru.kpfu.itis.carwash.databinding.ItemReviewBinding
import ru.kpfu.itis.carwash.map.model.Review

class ReviewsHolder(
    private val binding: ItemReviewBinding,
): RecyclerView.ViewHolder(binding.root) {

    fun bind(review: Review) {
        binding.name.text = review.fullName.first().toString()
        binding.fullName.text = review.fullName
        binding.reviewText.text = review.review
        binding.rating.rating = review.rating.toFloat()
        binding.time.text = review.time
    }

    companion object {
        fun create(parent: ViewGroup): ReviewsHolder =
            ReviewsHolder(
                DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_review, parent, false),
            )

    }

}