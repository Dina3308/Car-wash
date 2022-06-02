package ru.kpfu.itis.carwash.map

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.kpfu.itis.carwash.map.model.History

class HistoryAdapter(
    private var list: List<History>,
    private val itemClick: (History) -> Unit
) : RecyclerView.Adapter<HistoryHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryHolder =
        HistoryHolder.create(parent, itemClick)

    override fun onBindViewHolder(holder: HistoryHolder, position: Int) =
        holder.bind(list[position])

    override fun getItemCount(): Int = list.size
}