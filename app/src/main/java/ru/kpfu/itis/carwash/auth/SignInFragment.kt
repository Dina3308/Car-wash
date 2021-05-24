package ru.kpfu.itis.carwash.auth

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
import ru.kpfu.itis.carwash.auth.model.LoginForm
import ru.kpfu.itis.carwash.common.makeLinks
import ru.kpfu.itis.carwash.databinding.SignInFragmentBinding
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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity?.application as App).appComponent.signInComponentFactory()
            .create(this)
            .inject(this)

        initLoginClickListener()
        initSubscribes()
    }

    private fun initSubscribes() {
        viewModel.login().observe(
            viewLifecycleOwner,
            {
                findNavController().navigate(R.id.action_signInFragment_to_homeFragment)
            }
        )

        viewModel.progress().observe(
            viewLifecycleOwner,
            {
                binding.progressBar.isVisible = it
            }
        )

        viewModel.showErrorEvent().observe(
            viewLifecycleOwner,
            {
                showToast(it.peekContent())
            }
        )
    }

    private fun initLoginClickListener() {
        binding.signInBtn.setOnClickListener {
            with(binding) {
                viewModel.login(LoginForm(emailEdit.text.toString(), passwordEdit.text.toString()))
            }
        }

        binding.signUpTv.makeLinks(
            Pair(
                resources.getString(R.string.sign_up_link),
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
