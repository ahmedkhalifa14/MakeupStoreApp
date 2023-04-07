package com.example.makeupstore.ui.onboarding

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.makeupstore.R
import com.example.makeupstore.databinding.FragmentSplashBinding
import com.example.makeupstore.ui.home.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class SplashFragment : Fragment() {

    private var _binding: FragmentSplashBinding? = null
    private val binding get() = _binding
    private val mainViewModel: MainViewModel by viewModels()
    private var isFirstTimeLaunchVar: Boolean? = false
    private var isLogin: Boolean? = false


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentSplashBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.apply {
            mainViewModel.getUserInfo.observe(requireActivity()) { userInfo ->
                if (userInfo.token != null) {
                    isLogin = true
                    userInfo.token.let { Timber.tag("token").d(it) }
                }
            }
        }
        binding.apply {
            mainViewModel.isFirstTimeLaunch.observe(requireActivity()) { isFirstTimeLaunch ->
                Timber.tag("token").d(isFirstTimeLaunch.toString())
                isFirstTimeLaunchVar = when (isFirstTimeLaunch) {
                    true -> {
                        true

                    }
                    false -> {
                        false
                    }
                }
            }
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        try {
            Handler(Looper.myLooper()!!).postDelayed(

                {
                    if (isFirstTimeLaunchVar == true && isLogin == true) {
                        lifecycleScope.launchWhenResumed {
                            findNavController().navigate(R.id.action_splashFragment_to_homeFragment)
                        }


                    } else if (isFirstTimeLaunchVar == true && isLogin == false) {
                        lifecycleScope.launchWhenResumed {
                            findNavController().navigate(R.id.action_splashFragment_to_registerFragment)
                        }
                    } else {
                        lifecycleScope.launchWhenResumed {
                            findNavController().navigate(R.id.action_splashFragment_to_onboardingFragment)
                        }
                    }
                }, 4000
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}