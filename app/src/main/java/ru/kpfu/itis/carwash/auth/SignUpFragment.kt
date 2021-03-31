package ru.kpfu.itis.carwash.auth

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import ru.kpfu.itis.carwash.R
import ru.kpfu.itis.carwash.ViewModelFactory
import ru.kpfu.itis.carwash.databinding.SignUpFragmentBinding
import ru.kpfu.itis.data.api.places.ApiFactory
import ru.kpfu.itis.data.repository.AuthRepositoryImpl
import ru.kpfu.itis.data.repository.LocationRepositoryImpl
import ru.kpfu.itis.domain.AuthInteractor
import ru.kpfu.itis.domain.model.AuthUser
import ru.kpfu.itis.domain.model.Resource

class SignUpFragment : Fragment() {

    private lateinit var viewModel: AuthViewModel
    private lateinit var binding: SignUpFragmentBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.sign_up_fragment, container, false)
        viewModel = ViewModelProvider(this, initFactory()).get(AuthViewModel::class.java)
        initRegisterClickListener()
        return binding.root
    }

    private fun initFactory() = ViewModelFactory(
        ApiFactory.placesService,
        LocationRepositoryImpl(
            client = LocationServices.getFusedLocationProviderClient(requireActivity())
        ),
        AuthInteractor(
            AuthRepositoryImpl(FirebaseAuth.getInstance())
        )
    )

    private fun registerObserver(authUser: AuthUser) {
        viewModel.register(authUser).observe(viewLifecycleOwner,  {result ->
            when(result){
                is Resource.Loading ->{
                    showLoading()
                }
                is Resource.Success -> {
                    hideLoading()
                    showToast("успешно")
                }
                is Resource.Error -> {
                    hideLoading()
                    result.error?.message?.let { showToast(it) }
                }
            }
        })
    }

    private fun initRegisterClickListener(){
        binding.registerBtn.setOnClickListener {
            with(binding){
                val email = emailEdit.text.toString()
                val password = passwordEdit.text.toString()
                val passwordRepeat = passwordRepeatEdit.text.toString()
                when {
                    email.isEmpty() -> emailEdit.error = "Please enter email"
                    password.isEmpty() -> passwordEdit.error = "Please enter password"
                    passwordRepeat.isEmpty() -> passwordRepeatEdit.error = "Please enter password"
                    password != passwordRepeat -> showToast("no correct password")
                    else -> {
                        registerObserver(AuthUser(email, password))
                    }
                }
            }
        }
    }

    private  fun showLoading(){
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun hideLoading(){
        binding.progressBar.visibility = View.GONE
    }

    private fun showToast(message: String){
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}