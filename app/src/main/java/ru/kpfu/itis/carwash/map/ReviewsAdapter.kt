package ru.kpfu.itis.carwash.map

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.kpfu.itis.carwash.map.model.Review

class ReviewsAdapter(
    private var list: List<Review>,
) : RecyclerView.Adapter<ReviewsHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewsHolder =
        ReviewsHolder.create(parent)

    override fun onBindViewHolder(holder: ReviewsHolder, position: Int) =
        holder.bind(list[position])

    override fun getItemCount(): Int = list.size
}