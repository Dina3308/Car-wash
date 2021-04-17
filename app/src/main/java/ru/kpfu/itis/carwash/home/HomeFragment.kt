package ru.kpfu.itis.carwash.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import ru.kpfu.itis.carwash.App
import ru.kpfu.itis.carwash.R
import ru.kpfu.itis.carwash.databinding.HomeFragmentBinding
import javax.inject.Inject

class HomeFragment : Fragment() {

    @Inject
    lateinit var viewModel: HomeViewModel
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
        }
    }

    private fun initClickListener() {
        with(viewModel) {
            binding.mainToolbar.right2IconClickListener {
                signOut()
            }
            binding.mainToolbar.right1IconClickListener {
                findNavController().navigate(R.id.action_homeFragment_to_settingsFragment)
            }
        }
    }
}
