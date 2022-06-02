package ru.kpfu.itis.carwash.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import ru.kpfu.itis.carwash.App
import ru.kpfu.itis.carwash.R
import ru.kpfu.itis.carwash.databinding.FragmentHistoryBinding
import ru.kpfu.itis.carwash.map.model.History
import javax.inject.Inject

class HistoryFragment() : Fragment() {

    private lateinit var binding: FragmentHistoryBinding

    @Inject
    lateinit var viewModel: MapsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity?.application as App).appComponent.mapsComponentFactory()
            .create(this)
            .inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_history, container, false)
        binding.mainToolbar.leftIconClickListener {
            findNavController().navigate(R.id.action_historyFragment_to_mapsFragment)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val histories = listOf(
            History(
                "Авто-сити",
                "14.04.2022 10:30"
            ),
            History(
                "Авто-сити",
                "2.03.2022 12:30"
            ),
            History(
                "Мой Car",
                "01.03.2022 09:30"
            ),
            History(
                "Мой Car",
                "15.02.2022 09:30",
            ),
            History(
                "Чистомойка",
                "03.02.2022 10:30"
            )
        )
        binding.rv.adapter = HistoryAdapter(
            histories
        ) {

        }
    }
}