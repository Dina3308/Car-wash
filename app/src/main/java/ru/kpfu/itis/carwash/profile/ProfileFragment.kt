package ru.kpfu.itis.carwash.profile

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.bumptech.glide.Glide
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.MaterialDatePicker
import com.xw.repo.BubbleSeekBar
import com.xw.repo.BubbleSeekBar.OnProgressChangedListenerAdapter
import ru.kpfu.itis.carwash.App
import ru.kpfu.itis.carwash.R
import ru.kpfu.itis.carwash.databinding.DialogUpdateLevelOfCarPollutionBinding
import ru.kpfu.itis.carwash.databinding.HomeFragmentBinding
import ru.kpfu.itis.carwash.profile.model.CurrentWeatherDetails
import ru.kpfu.itis.carwash.profile.workManager.DailyWeatherCheckWorker
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class ProfileFragment : Fragment() {

    companion object {
        private const val REPEAT_INTERVAL = 1L
    }
    @Inject
    lateinit var viewModel: ProfileViewModel
    private lateinit var binding: HomeFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.home_fragment, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity?.application as App).appComponent.homeComponentFactory()
            .create(this)
            .inject(this)

        initSubscribes()
        initClickListener()
        initWorkManager()
        initSeekBar()
    }

    private fun initSubscribes() {
        with(viewModel) {
            progress().observe(
                viewLifecycleOwner,
                {
                    binding.progressBar.isVisible = it
                }
            )

            signOut().observe(
                viewLifecycleOwner,
                {
                    findNavController().navigate(R.id.action_homeFragment_to_signInFragment)
                }
            )

            date().observe(
                viewLifecycleOwner,
                {
                    binding.dateEdit.setText(it.getContentIfNotHandled())
                }
            )

            user().observe(
                viewLifecycleOwner,
                {
                    binding.dateEdit.setText(it.date)
                    showWeather(it.lat, it.lon)
                    getDateCarWash(it.lat, it.lon)
                    binding.seekBar.setProgress(it.levelOfCarPollution)
                }
            )

            weather().observe(
                viewLifecycleOwner,
                {
                    setWeather(it)
                }
            )

            dateCarWash().observe(
                viewLifecycleOwner,
                {
                    binding.notificationCarWashTv.text = it.getContentIfNotHandled()
                    binding.notificationCarWashCv.visibility = View.VISIBLE
                }
            )

            levelOfCarPollution().observe(
                viewLifecycleOwner,
                {
                    binding.seekBar.setProgress(it)
                }
            )

            showDialogEvent().observe(
                viewLifecycleOwner,
                {
                    showAlterDialog()
                }
            )

            showErrorEvent().observe(
                viewLifecycleOwner,
                {
                    showToast(it.peekContent())
                }
            )
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initClickListener() {
        with(viewModel) {
            binding.mainToolbar.right2IconClickListener {
                signOutUser()
            }

            binding.mainToolbar.right1IconClickListener {
                findNavController().navigate(R.id.action_homeFragment_to_settingsFragment)
            }
        }

        binding.dateEdit.setOnClickListener {
            showDatePicker()
        }

        binding.carWashBtn.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_mapsFragment)
        }
    }

    private fun initWorkManager() {
        val constraints = Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
        val periodicWorkRequest = PeriodicWorkRequest.Builder(
            DailyWeatherCheckWorker::class.java,
            REPEAT_INTERVAL,
            TimeUnit.DAYS
        ).setConstraints(constraints).build()
        activity?.applicationContext?.let { context ->
            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                resources.getString(R.string.work_manager_name),
                ExistingPeriodicWorkPolicy.KEEP,
                periodicWorkRequest
            )
        }
    }

    private fun initSeekBar() {
        binding.seekBar.setCustomSectionTextArray { _, array ->
            array.clear()
            array.put(0, resources.getString(R.string.first_level))
            array.put(1, resources.getString(R.string.second_level))
            array.put(2, resources.getString(R.string.third_level))
            array
        }

        binding.seekBar.onProgressChangedListener = object : OnProgressChangedListenerAdapter() {
            override fun getProgressOnActionUp(
                bubbleSeekBar: BubbleSeekBar?,
                progress: Int,
                progressFloat: Float
            ) {
                viewModel.updateLevelOfCarPollution(progress)
            }
        }
    }

    private fun showDatePicker() {
        Locale.setDefault(Locale.forLanguageTag(resources.getString(R.string.language_tag)))

        val constraintsBuilder = CalendarConstraints.Builder()
            .setValidator(DateValidatorPointBackward.now())
            .build()

        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText(resources.getString(R.string.choose_date))
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
            .setCalendarConstraints(constraintsBuilder)
            .build()

        datePicker.addOnPositiveButtonClickListener {
            viewModel.updateDate(Date(it))
        }

        datePicker.show(childFragmentManager, resources.getString(R.string.date_picker_tag))
    }

    private fun setWeather(weather: CurrentWeatherDetails) {
        with(binding) {
            currentCityTv.text = weather.name
            currentDateTv.text = weather.date
            tempTv.text = getString(R.string.temp_tv, weather.temp.toInt())
            maxMinTv.text = getString(
                R.string.max_min_tv,
                weather.tempMin.toInt(),
                weather.tempMax.toInt()
            )
            descriptionTv.text = weather.description
            context?.let { it1 ->
                Glide
                    .with(it1)
                    .load(resources.getString(R.string.url_for_upload, weather.icon))
                    .into(weatherIv)
            }
            weatherCardView.visibility = View.VISIBLE
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun showAlterDialog() {
        val alertDialog: AlertDialog? = activity?.let {
            val builder = AlertDialog.Builder(it)
            val bindingDialog: DialogUpdateLevelOfCarPollutionBinding = DataBindingUtil.inflate(LayoutInflater.from(requireContext()), R.layout.dialog_update_level_of_car_pollution, null, false)
            builder.apply {
                setTitle(resources.getString(R.string.title_dialog))
                setView(bindingDialog.root)

                setPositiveButton(
                    R.string.ok,
                    DialogInterface.OnClickListener { dialog, id ->
                        viewModel.updateLevelOfCarPollution(bindingDialog.slider.value.toInt())
                    }
                )
            }

            builder.show()
        }
    }
}
