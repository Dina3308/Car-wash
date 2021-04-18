package ru.kpfu.itis.carwash.profile

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.datepicker.MaterialDatePicker
import ru.kpfu.itis.carwash.App
import ru.kpfu.itis.carwash.R
import ru.kpfu.itis.carwash.common.dateFormatter
import ru.kpfu.itis.carwash.databinding.HomeFragmentBinding
import java.util.*
import javax.inject.Inject

class ProfileFragment : Fragment() {

    @Inject
    lateinit var viewModel: ProfileViewModel
    private lateinit var binding: HomeFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.home_fragment, container, false)
        initSubscribes()
        initClickListener()
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        (activity?.application as App).appComponent.homeComponentFactory()
            .create(this)
            .inject(this)
    }

    private fun initSubscribes() {
        with(viewModel) {
            progress().observe(
                viewLifecycleOwner,
                {
                    binding.progressBar.isVisible = it
                }
            )
            exit().observe(
                viewLifecycleOwner,
                {
                    try {
                        it.getOrThrow()
                        findNavController().navigate(R.id.action_homeFragment_to_signInFragment)
                    } catch (throwable: Throwable) {
                    }
                }
            )

            date().observe(viewLifecycleOwner, { result ->
                try {
                    result.getOrThrow().also {
                        binding.dateEdit.dateFormatter(it)
                    }
                } catch (throwable: Throwable) {

                }
            })

            user().observe(viewLifecycleOwner, { result ->
                try {
                    result.getOrThrow().getDate("date").run {
                        if(this != null){
                            binding.dateEdit.dateFormatter(this)
                        }
                        else{
                            binding.dateEdit.setText("Выберите дату")
                        }
                    }
                } catch (throwable: Throwable) {

                }
            })
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initClickListener() {
        with(viewModel) {
            binding.mainToolbar.right2IconClickListener {
                signOut()
            }

            binding.mainToolbar.right1IconClickListener {
                findNavController().navigate(R.id.action_homeFragment_to_settingsFragment)
            }

        }

        with(binding.dateEdit) {
            setOnTouchListener { v, event ->
                if (event.action == MotionEvent.ACTION_UP) {
                    if (event.rawX >= (right - compoundDrawables[2].bounds.width())) {
                        showDatePicker()
                        return@setOnTouchListener true
                    }
                }
                return@setOnTouchListener false
            }
        }
    }

    private fun showDatePicker() {
        Locale.setDefault(Locale.forLanguageTag("ru"))

        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Выберите дату")
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
            .build()

        datePicker.addOnPositiveButtonClickListener {
            viewModel.setDate(Date(it))
        }

        datePicker.show(childFragmentManager, "datePicker")

    }
}
