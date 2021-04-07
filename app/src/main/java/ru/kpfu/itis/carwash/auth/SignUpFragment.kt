package ru.kpfu.itis.carwash.auth

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import ru.kpfu.itis.carwash.App
import ru.kpfu.itis.carwash.R
import ru.kpfu.itis.carwash.common.ResultState
import ru.kpfu.itis.carwash.databinding.SignUpFragmentBinding
import ru.kpfu.itis.domain.model.AuthUser
import javax.inject.Inject

class SignUpFragment : Fragment() {

    @Inject
    lateinit var viewModel: AuthViewModel
    private lateinit var binding: SignUpFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.sign_up_fragment, container, false)
        initRegisterClickListener()
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity?.application as App).appComponent.signUpComponentFactory()
            .create(this)
            .inject(this)
    }

    private fun registerObserver(authUser: AuthUser) {
        viewModel.register(authUser).observe(
            viewLifecycleOwner,
            { result ->
                when (result) {
                    is ResultState.Loading -> {
                        showLoading()
                    }
                    is ResultState.Success<*> -> {
                        hideLoading()
                        showToast("успешно")
                    }
                    is ResultState.Error<*> -> {
                        hideLoading()
                        result.error.message?.let { showToast(it) }
                    }
                }
            }
        )
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
                    else -> {
                        registerObserver(AuthUser(email, password))
                    }
                }
            }
        }
    }

    private fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        binding.progressBar.visibility = View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}
