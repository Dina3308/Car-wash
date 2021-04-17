package ru.kpfu.itis.carwash.auth

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import ru.kpfu.itis.carwash.App
import ru.kpfu.itis.carwash.R
import ru.kpfu.itis.carwash.common.ResultState
import ru.kpfu.itis.carwash.common.makeLinks
import ru.kpfu.itis.carwash.databinding.SignInFragmentBinding
import ru.kpfu.itis.domain.model.AuthUser
import javax.inject.Inject

class SignInFragment : Fragment() {

    @Inject
    lateinit var viewModel: AuthViewModel
    private lateinit var binding: SignInFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.sign_in_fragment, container, false)
        initLoginClickListener()
        initSubscribes()
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity?.application as App).appComponent.signInComponentFactory()
            .create(this)
            .inject(this)
    }

    private fun initSubscribes() {
        viewModel.login().observe(viewLifecycleOwner, {
            try {
                it.getOrThrow().run {
                    findNavController().navigate(R.id.action_signInFragment_to_homeFragment)
                }
            }catch (throwable: Throwable){

            }
        })

        viewModel.progress().observe(viewLifecycleOwner, {
            binding.progressBar.isVisible = it
        })
    }

    private fun initLoginClickListener() {
        binding.signInBtn.setOnClickListener {
            with(binding) {
                val email = emailEdit.text.toString()
                val password = passwordEdit.text.toString()
                when {
                    email.isEmpty() -> emailEdit.error = "Please enter email"
                    password.isEmpty() -> passwordEdit.error = "Please enter password"
                    else -> {
                        viewModel.login(AuthUser(email, password))
                    }
                }
            }
        }

        binding.signUpTv.makeLinks(
            Pair(
                "Зарегистрируйтесь",
                View.OnClickListener {
                    findNavController().navigate(R.id.action_signInFragment_to_signUpFragment)
                }
            )
        )
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}
