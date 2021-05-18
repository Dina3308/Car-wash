package ru.kpfu.itis.carwash.splash_screen

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import ru.kpfu.itis.carwash.App
import ru.kpfu.itis.carwash.R
import javax.inject.Inject

class SplashFragment : Fragment() {
    @Inject
    lateinit var viewModel: SplashViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_splash, container, false)
        initSubscribes()
        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        (activity?.application as App).appComponent.splashComponentFactory()
            .create(this)
            .inject(this)
    }

    private fun initSubscribes() {
        with(viewModel) {
            user().observe(
                viewLifecycleOwner,
                {
                    findNavController().navigate(
                        if (it) {
                            R.id.action_splashFragment_to_homeFragment
                        } else {
                            R.id.action_splashFragment_to_signInFragment
                        }
                    )
                }
            )
        }
    }
}
