package ru.kpfu.itis.carwash.setting

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import ru.kpfu.itis.carwash.App
import ru.kpfu.itis.carwash.BuildConfig
import ru.kpfu.itis.carwash.R
import ru.kpfu.itis.carwash.databinding.FragmentSettingsBinding
import java.util.*
import javax.inject.Inject

class SettingFragment : Fragment() {

    companion object {
        private const val AUTOCOMPLETE_REQUEST_CODE = 45
    }
    @Inject
    lateinit var viewModel: SettingViewModel
    private lateinit var binding: FragmentSettingsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_settings, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity?.application as App).appComponent.settingComponentFactory()
            .create(this)
            .inject(this)

        initClickListener()
        initSubscribes()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            when (resultCode) {
                RESULT_OK -> {
                    data?.let {
                        viewModel.updateCity(Autocomplete.getPlaceFromIntent(it))
                    }
                }
            }
        }
    }

    private fun startAutocompleteActivity() {
        activity?.applicationContext?.let { Places.initialize(it, BuildConfig.API_KEY, Locale(resources.getString(R.string.language_tag))) }
        val intent = activity?.applicationContext?.let {
            Autocomplete.IntentBuilder(
                AutocompleteActivityMode.OVERLAY,
                listOf(Place.Field.ADDRESS, Place.Field.LAT_LNG)
            )
                .setTypeFilter(TypeFilter.CITIES)
                .build(it)
        }
        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE)
    }

    private fun initClickListener() {
        binding.toolbar.leftIconClickListener {
            findNavController().navigate(R.id.action_settingsFragment_to_homeFragment)
        }

        binding.cityTv.setOnClickListener {
            startAutocompleteActivity()
        }
    }

    private fun initSubscribes() {
        with(viewModel) {
            progress().observe(
                viewLifecycleOwner,
                {
                    binding.progressBar.isVisible = it
                }
            )

            cityAddress().observe(
                viewLifecycleOwner,
                {
                    binding.cityTv.text = it
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

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}
