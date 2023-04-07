package com.example.makeupstore.ui.login

import android.text.TextUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.makeupstore.helper.NetworkControl
import com.example.makeupstore.models.User
import com.example.makeupstore.repository.AuthRepo
import com.example.makeupstore.utils.Resource
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: AuthRepo,
    private val networkControl: NetworkControl,
    private val firebaseAuth: FirebaseAuth
) :
    ViewModel() {

    private val _userLiveData = MutableLiveData<Resource<User>>()
    val userLiveData: LiveData<Resource<User>>
        get() = _userLiveData
    private val gMailUserLiveData = MutableLiveData<Resource<User>>()
    private val sendResetPasswordLiveData = MutableLiveData<Resource<User>>()
    fun signInUser(email: String, password: String): LiveData<Resource<User>> {
        when {
            TextUtils.isEmpty(email) && TextUtils.isEmpty(password) -> {
                _userLiveData.postValue(Resource.Error(null, "Enter email and password"))
            }
            networkControl.isConnected() -> {
                _userLiveData.postValue(Resource.Loading(null))
                firebaseAuth.fetchSignInMethodsForEmail(email).addOnCompleteListener { it ->
                    //check if email exists
                    if (it.result?.signInMethods?.size == 0) {
                        _userLiveData.postValue(Resource.Error(null, "Email does not exist"))
                    } else {
                        repository.signInUser(email, password).addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                firebaseAuth.currentUser?.isEmailVerified?.let { verified ->
                                    if (verified) {
                                        repository.fetchUser().addOnCompleteListener { userTask ->
                                            if (userTask.isSuccessful) {
                                                userTask.result?.documents?.forEach {
                                                    if (it.data!!["email"] == email) {
                                                        val name = it.data?.getValue("name")
                                                        _userLiveData.postValue(
                                                            Resource.Success(
                                                                User(
                                                                    firebaseAuth.currentUser?.email!!,
                                                                    name?.toString()!!
                                                                )
                                                            )
                                                        )
                                                    }
                                                }
                                            } else {
                                                _userLiveData.postValue(
                                                    Resource.Error(
                                                        null,
                                                        userTask.exception?.message.toString()
                                                    )
                                                )
                                            }
                                        }
                                    } else {
                                        _userLiveData.postValue(
                                            Resource.Error(
                                                null,
                                                "Email is not verified, check your email"
                                            )
                                        )
                                    }
                                }
                            } else {
                                _userLiveData.postValue(
                                    Resource.Error(
                                        null,
                                        task.exception?.message.toString()
                                    )
                                )
                                Timber.e(task.exception.toString())
                            }
                        }
                    }
                }
            }
            else -> {
                _userLiveData.postValue(Resource.Error(null, "No internet connection"))
            }
        }
        return userLiveData
    }

    fun signInWithGoogle(acct: GoogleSignInAccount): LiveData<Resource<User>> {
        repository.signInWithGoogle(acct).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                gMailUserLiveData.postValue(
                    Resource.Success(
                        User(
                            firebaseAuth.currentUser?.email!!,
                            firebaseAuth.currentUser?.displayName!!
                        )
                    )
                )
            } else {
                gMailUserLiveData.postValue(Resource.Error(null, "couldn't sign in user"))
            }

        }
        return gMailUserLiveData
    }

    fun sendResetPassword(email: String): LiveData<Resource<User>> {

        when {
            TextUtils.isEmpty(email) -> {
                sendResetPasswordLiveData.postValue(Resource.Error(null, "Enter registered email"))
            }
            networkControl.isConnected() -> {
                repository.sendForgotPassword(email).addOnCompleteListener { task ->
                    sendResetPasswordLiveData.postValue(Resource.Loading(null))
                    if (task.isSuccessful) {
                        sendResetPasswordLiveData.postValue(Resource.Success(User()))
                    } else {
                        sendResetPasswordLiveData.postValue(
                            Resource.Error(
                                null,
                                task.exception?.message.toString()
                            )
                        )
                    }
                }
            }
            else -> {
                sendResetPasswordLiveData.postValue(Resource.Error(null, "No internet connection"))
            }
        }
        return sendResetPasswordLiveData
    }
}