package ru.kpfu.itis.carwash.map

import android.content.DialogInterface
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.tabs.TabLayout
import ru.kpfu.itis.carwash.R
import ru.kpfu.itis.carwash.databinding.BottomSheetCarWashDetailsBinding
import ru.kpfu.itis.carwash.databinding.DialogUpdateLevelOfCarPollutionBinding
import ru.kpfu.itis.carwash.map.model.CarWash

private const val CAR_WASH_DETAILS_KEY = "carWash"

class BottomSheetFragment() : BottomSheetDialogFragment() {

    private lateinit var binding: BottomSheetCarWashDetailsBinding

    private val carWash by lazy { requireArguments().let { BottomSheetFragmentArgs.fromBundle(it).carWash } }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.bottom_sheet_car_wash_details, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.setOnShowListener { dialog ->
            val bottomSheetDialog = dialog as BottomSheetDialog
            val bottomSheet = bottomSheetDialog
                .findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as ViewGroup
            val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
            bottomSheetBehavior.peekHeight = 650
        }
        binding.address.text = carWash.address
        binding.carWashName.text = carWash.name
        binding.btn.setOnClickListener {
            showAlterDialog(carWash.name, carWash.address)
        }
        initViewPager()
        initTabLayout()
    }

    private fun initTabLayout() {
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                binding.viewPager.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) = Unit

            override fun onTabReselected(tab: TabLayout.Tab?) = Unit
        })
    }

    private fun initViewPager() {
        binding.viewPager.adapter = CarWashAdapter(
            listOf(CarWashDetailsFragment(carWash), ReviewsFragment()),
            this
        )

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                binding.tabLayout.selectTab(binding.tabLayout.getTabAt(position))
            }
        })
    }

    private fun showAlterDialog(name: String, address: String) {
        val alertDialog: AlertDialog? = activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.apply {
                setMessage("Вы записаны на автомойку $name по адресу $address в 13:00")
                setPositiveButton(
                    R.string.ok,
                    DialogInterface.OnClickListener { dialog, id ->
                        dialog.cancel()
                    }
                )
            }

            builder.show()
        }
    }
}