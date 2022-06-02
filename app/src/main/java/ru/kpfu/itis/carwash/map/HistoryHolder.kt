package ru.kpfu.itis.carwash.map

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import ru.kpfu.itis.carwash.R
import ru.kpfu.itis.carwash.databinding.ItemHistoryBinding
import ru.kpfu.itis.carwash.map.model.History


class HistoryHolder (
    private val binding: ItemHistoryBinding,
    private val itemClick: (History) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    private var history: History? = null

    init {
        itemView.setOnClickListener {
            history?.also(itemClick)
        }
    }

    fun bind(history: History) {
        this.history = history
        with(history) {
            binding.carWahName.text = carWashName
            binding.date.text = date
        }
    }

    companion object {

        fun create(parent: ViewGroup, itemClick: (History) -> Unit): HistoryHolder =
            HistoryHolder(
                DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_history, parent, false),
                itemClick
            )
    }
}