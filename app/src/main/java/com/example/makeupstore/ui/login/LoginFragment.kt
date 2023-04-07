package com.example.makeupstore.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.makeupstore.R
import com.example.makeupstore.databinding.FragmentLoginBinding
import com.example.makeupstore.models.User
import com.example.makeupstore.ui.home.MainViewModel
import com.example.makeupstore.ui.register.RegisterViewModel
import com.example.makeupstore.utils.Constants
import com.example.makeupstore.utils.Constants.RC_SIGN_IN
import com.example.makeupstore.utils.Resource
import com.example.makeupstore.utils.Utils.showSnackBar
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding
    private val viewModel: LoginViewModel by viewModels()
    private val mainViewModel: MainViewModel by viewModels()
    private val registerViewModel: RegisterViewModel by viewModels()

    @Inject
    lateinit var auth: FirebaseAuth
    private val TAG = "LoginFragment"

    @Inject
    lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding!!.loginBtn.setOnClickListener {
            val emailText = binding?.emailEt?.text?.toString()
            val passwordText = binding?.passwordEt?.text.toString()

            viewModel.signInUser(emailText!!, passwordText)
        }
        binding!!.backIcon.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
        binding!!.singupGoogleBtn.setOnClickListener {
            signIn()
        }
        binding!!.textSignUp.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)

        }
        subscribeToObservers()
    }

    private fun signIn() {

        val signInIntent: Intent = googleSignInClient.signInIntent

        startActivityForResult(signInIntent, RC_SIGN_IN)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constants.RC_SIGN_IN) {


            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {

                val account = task.getResult(ApiException::class.java)


                viewModel.signInWithGoogle(account!!).observe(viewLifecycleOwner) {

                    when (it) {
                        is Resource.Loading -> {
                            binding!!.spinKit.isVisible = true

                        }
                        is Resource.Success -> {
                            registerViewModel.saveUser(
                                auth.currentUser?.email!!,
                                auth.currentUser?.displayName!!,
                                "",
                                "",
                                auth.currentUser?.uid!!

                            )
                            mainViewModel.setUserINfo(
                                User(
                                    auth.currentUser?.email!!,
                                    auth.currentUser?.displayName!!,
                                    "",
                                    "",
                                    auth.currentUser?.uid!!
                                )
                            )
                            binding!!.spinKit.isVisible = false

                            if (findNavController().currentDestination?.id == R.id.loginFragment) {
                                NavHostFragment.findNavController(this).navigate(
                                    LoginFragmentDirections.actionLoginFragmentToHomeFragment()
                                )
                            }

                        }
                        is Resource.Error -> {
                            binding!!.spinKit.isVisible = false

                            requireView().showSnackBar(it.message!!)

                        }
                        else -> {}
                    }

                }
            } catch (e: ApiException) {
                binding!!.spinKit.isVisible = false
                Toast.makeText(requireContext(), e.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun subscribeToObservers() {
        viewModel.userLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {
                    binding!!.spinKit.isVisible = true
                }
                is Resource.Success -> {
                    binding!!.spinKit.isVisible = false
                    view?.showSnackBar("Login successfully")
                    it.data.let {
                        lifecycleScope.launch {
                            if (it != null) {
                                saveUserInfoAndNavigate(it)
                            }
                        }
                    }

                }
                is Resource.Error -> {
                    binding!!.spinKit.isVisible = false
                    view?.showSnackBar(it.message!!)

                }
                else -> {}
            }
        }
    }

    private suspend fun saveUserInfoAndNavigate(user: User) {
        val userEmail = binding!!.inputTextLayoutPassword.editText!!.text.toString()
        val userPassword = binding!!.inputTextLayoutPassword.editText!!.text.toString()
        val userToken = auth.currentUser!!.uid.toString()
        updateUserToken(userEmail, userPassword, userToken)
        if (binding!!.switchRememberMe.isChecked) {
            Timber.tag(TAG).d(binding!!.switchRememberMe.isChecked.toString())
            saveEmailAndPassword(userEmail, userPassword)
            Timber.tag(TAG).d("%s%s", userEmail, userPassword)
        }
        if (findNavController().currentDestination?.id == R.id.loginFragment) {
            NavHostFragment.findNavController(this)
                .navigate(
                    LoginFragmentDirections.actionLoginFragmentToHomeFragment()
                )
        }
    }

    private suspend fun updateUserToken(
        userEmail: String,
        userPassword: String,
        userToken: String
    ) {
        mainViewModel.setUserINfo(User(userEmail, "", userPassword, "", userToken))
    }

    private fun saveEmailAndPassword(userEmail: String, userPassword: String) {
        lifecycleScope.launch {
            mainViewModel.setUserINfo(User(userEmail, userPassword))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}