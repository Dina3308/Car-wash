package ru.kpfu.itis.carwash.auth

import android.app.Activity
import android.content.Context
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
import ru.kpfu.itis.carwash.common.makeLinks
import ru.kpfu.itis.carwash.databinding.SignUpFragmentBinding
import ru.kpfu.itis.domain.model.AuthUser
import java.util.*
import javax.inject.Inject

class SignUpFragment : Fragment() {

    @Inject
    lateinit var viewModel: AuthViewModel
    private lateinit var binding: SignUpFragmentBinding
    private lateinit var place: Place

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.sign_up_fragment, container, false)
        initRegisterClickListener()
        initSubscribes()
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity?.application as App).appComponent.signUpComponentFactory()
            .create(this)
            .inject(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            when (resultCode) {
                Activity.RESULT_OK -> {
                    val result = data?.let { Autocomplete.getPlaceFromIntent(it) }
                    result?.let {
                        binding.searchCityEdit.setText(it.address)
                        place = it
                    }
                }
            }
        }
    }

    private fun startAutocompleteActivity() {
        activity?.applicationContext?.let { Places.initialize(it, BuildConfig.API_KEY, Locale("ru")) }
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

    private fun initSubscribes() {
        viewModel.register().observe(viewLifecycleOwner, {
            try {
                it.getOrThrow().run {
                    viewModel.addCity(place, uid)
                }
            }catch (throwable: Throwable){

            }
        })

        viewModel.city().observe(viewLifecycleOwner, {
            try {
                it.getOrThrow().run {
                    showToast("успешно")
                    findNavController().navigate(R.id.action_signUpFragment_to_homeFragment)
                }
            }catch (throwable: Throwable){

            }
        })

        viewModel.progress().observe(viewLifecycleOwner, {
            binding.progressBar.isVisible = it
        })
    }


    private fun initRegisterClickListener() {
        binding.registerBtn.setOnClickListener {
            with(binding) {
                val email = emailEdit.text.toString()
                val password = passwordEdit.text.toString()
                val passwordRepeat = passwordRepeatEdit.text.toString()
                when {
                    email.isEmpty() -> emailEdit.error = "Please enter email"
                    password.isEmpty() -> passwordEdit.error = "Please enter password"
                    passwordRepeat.isEmpty() -> passwordRepeatEdit.error = "Please enter password"
                    password != passwordRepeat -> showToast("no correct password")
                    searchCityEdit.text.toString().isEmpty() -> searchCityEdit.error = "Please choose city"
                    else -> {
                        viewModel.register(AuthUser(email, password))
                    }
                }
            }
        }

        binding.signIn.makeLinks(
            Pair(
                "Войти",
                View.OnClickListener {
                    findNavController().navigate(R.id.action_signUpFragment_to_signInFragment)
                }
            )
        )

        binding.searchCityEdit.setOnClickListener {
            startAutocompleteActivity()
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val AUTOCOMPLETE_REQUEST_CODE = 46
    }
}
