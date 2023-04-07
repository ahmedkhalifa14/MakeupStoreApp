package com.example.makeupstore.ui.register

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.makeupstore.R
import com.example.makeupstore.databinding.FragmentRegisterBinding
import com.example.makeupstore.models.User
import com.example.makeupstore.ui.home.MainViewModel
import com.example.makeupstore.ui.login.LoginViewModel
import com.example.makeupstore.utils.Constants
import com.example.makeupstore.utils.Resource
import com.example.makeupstore.utils.Utils.showSnackBar
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding
    private val viewModel: RegisterViewModel by viewModels()
    private val loginViewModel: LoginViewModel by viewModels()
    private val mainViewModel: MainViewModel by viewModels()

    @Inject
    lateinit var googleSignInClient: GoogleSignInClient

    @Inject
    lateinit var auth: FirebaseAuth

    //project-240125747029
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.createAccountBtn?.setOnClickListener {
            val emailText = binding!!.inputTextLayoutEmail.editText!!.text.toString()
            val passwordText = binding!!.inputTextLayoutPassword.editText!!.text.toString()
            val fullNameText = binding!!.inputTextLayoutUserName.editText!!.text.toString()
            viewModel.signUpUser(emailText.toString(), passwordText, fullNameText.toString())


        }
        binding!!.alreadyHaveAccount.setOnClickListener {
            if (findNavController().currentDestination?.id == R.id.registerFragment) {
                NavHostFragment.findNavController(this)
                    .navigate(RegisterFragmentDirections.actionRegisterFragmentToHomeFragment())
            }
        }
        binding?.singupGoogleBtn?.setOnClickListener {
            singUpGoogleBtn()
        }
        binding!!.backBtn.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }

        subscribeToObservers()

    }

    private fun subscribeToObservers() {
        viewModel.userLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {
                    binding!!.spinKit.isVisible = true

                }
                is Resource.Success -> {
                    viewModel.saveUser(
                        it.data?.email.toString(),
                        it.data?.name.toString(),
                        it.data?.password.toString(),
                        binding!!.inputTextLayoutPhone.editText!!.text.toString(),
                        auth.currentUser!!.uid.toString()
                    )
                    mainViewModel.setUserINfo(
                        User(
                            it.data?.email.toString(),
                            it.data?.name.toString(),
                            it.data?.password.toString(),
                            binding!!.inputTextLayoutPhone.editText!!.text.toString(),
                            auth.currentUser!!.uid.toString()
                        )
                    )
                    binding!!.spinKit.isVisible = false
                    view?.showSnackBar("User account registered")
                    findNavController().navigate(R.id.action_registerFragment_to_loginFragment)

                }
                is Resource.Error -> {
                    binding!!.spinKit.isVisible = false
                    view?.showSnackBar(it.message!!)

                }
                else -> {}
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constants.RC_SIGN_IN) {


            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {

                val account = task.getResult(ApiException::class.java)


                loginViewModel.signInWithGoogle(account!!).observe(viewLifecycleOwner) {

                    when (it) {
                        is Resource.Loading ->{
                            binding!!.spinKit.isVisible=true
                        }
                        is Resource.Success -> {

                            viewModel.saveUser(
                                auth.currentUser?.email!!,
                                auth.currentUser?.displayName!!,
                                binding!!.inputTextLayoutPassword.editText!!.text.toString(),
                                binding!!.inputTextLayoutPhone.editText!!.text.toString(),
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
                            binding!!.spinKit.isVisible=false

                            if (findNavController().currentDestination?.id == R.id.registerFragment) {
                                NavHostFragment.findNavController(this).navigate(
                                    RegisterFragmentDirections.actionRegisterFragmentToHomeFragment()
                                )
                            }
                        }
                        is Resource.Error -> {
                            binding!!.spinKit.isVisible=false

                            requireView().showSnackBar(it.message!!)

                        }
                        else -> {}
                    }

                }
            } catch (e: ApiException) {
                binding!!.spinKit.isVisible=false
                Toast.makeText(requireContext(), e.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun singUpGoogleBtn() {
        val signInIntent: Intent = googleSignInClient.signInIntent

        startActivityForResult(signInIntent, Constants.RC_SIGN_IN)
    }
}