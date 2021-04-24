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
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.bumptech.glide.Glide
import com.google.android.material.datepicker.MaterialDatePicker
import ru.kpfu.itis.carwash.App
import ru.kpfu.itis.carwash.R
import ru.kpfu.itis.carwash.common.dateFormatter
import ru.kpfu.itis.carwash.common.setDate
import ru.kpfu.itis.carwash.databinding.HomeFragmentBinding
import ru.kpfu.itis.carwash.workManager.CarWashWorker
import java.util.*
import java.util.concurrent.TimeUnit
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
        initWorkManager()
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
                    Log.e("error", throwable.message.toString())
                }
            })

            user().observe(viewLifecycleOwner, { result ->
                try {
                    result.getOrThrow().also {
                        it.getDate("date").run {
                            if (this != null) {
                                binding.dateEdit.dateFormatter(this)
                            } else {
                                binding.dateEdit.setText("Выберите дату")
                            }
                        }

                        it.getGeoPoint("location")?.run {
                            showWeather(latitude, longitude)
                        }
                    }

                } catch (throwable: Throwable) {
                    Log.e("loc", throwable.message.toString())
                }
            })

            weather().observe(viewLifecycleOwner, { result ->
                try {
                    result.getOrThrow().also {
                        with(binding) {
                            currentCityTv.text = it.name
                            currentDateTv.setDate(Calendar.getInstance().time)
                            tempTv.text = getString(R.string.temp_tv, it.temp.toInt())
                            maxMinTv.text = getString(
                                R.string.max_min_tv,
                                it.tempMin.toInt(),
                                it.tempMax.toInt()
                            )
                            descriptionTv.text = it.description
                            context?.let { it1 ->
                                Glide
                                    .with(it1)
                                    .load("https://openweathermap.org/img/wn/${it.icon}@2x.png")
                                    .into(weatherIv)
                            }
                            weatherCardView.visibility = View.VISIBLE
                        }

                    }
                } catch (throwable: Throwable) {
                    Log.e("err", throwable.message.toString())
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

    private fun initWorkManager(){
        val constraints = Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
        val periodicWorkRequest = PeriodicWorkRequest.Builder(CarWashWorker::class.java, 10, TimeUnit.HOURS, 16, TimeUnit.HOURS).setConstraints(constraints).build()
        activity?.applicationContext?.let {context->
            WorkManager.getInstance(context).enqueueUniquePeriodicWork(CAR_WASH_WORK_NAME, ExistingPeriodicWorkPolicy.KEEP, periodicWorkRequest)
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

    companion object{
        private const val CAR_WASH_WORK_NAME = "carWash"
    }
}
