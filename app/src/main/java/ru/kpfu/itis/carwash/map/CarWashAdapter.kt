package ru.kpfu.itis.carwash.map

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class CarWashAdapter(
    private val collectionList: List<Fragment>,
    fragment: BottomSheetFragment
) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = collectionList.size

    override fun createFragment(position: Int) = collectionList[position]
}
