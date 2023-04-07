package com.example.makeupstore.ui.register

import android.text.TextUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.makeupstore.helper.NetworkControl
import com.example.makeupstore.models.User
import com.example.makeupstore.repository.AuthRepo
import com.example.makeupstore.utils.Resource
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val repository: AuthRepo,
    private val networkControl: NetworkControl,
    private val firebaseAuth: FirebaseAuth
):ViewModel(){

    private val _userLiveData = MutableLiveData<Resource<User>>()
    val userLiveData: LiveData<Resource<User>>
        get() = _userLiveData
    private val _saveUserLiveData = MutableLiveData<Resource<User>>()
    val saveUserLiveData = _saveUserLiveData
    fun signUpUser(email: String, password: String, fullName: String): LiveData<Resource<User>> {
        when {
            TextUtils.isEmpty(email) && TextUtils.isEmpty(password) && TextUtils.isEmpty( fullName ) -> {
                _userLiveData.postValue(Resource.Error(null, "field must not be empty"))
            }
            password.length < 8 -> {
                _userLiveData.postValue( Resource.Error(null, "password must not be less than 8"))
            }
            networkControl.isConnected() -> {
                _userLiveData.postValue(Resource.Loading(null))
                firebaseAuth.fetchSignInMethodsForEmail(email).addOnCompleteListener {
                    if (it.result?.signInMethods?.size == 0) {
                        repository.signUpUser(email, password, fullName).addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                firebaseAuth.currentUser?.sendEmailVerification()
                                _userLiveData.postValue( Resource.Success( User( email = email, name = fullName )))
                            } else {
                                _userLiveData.postValue( Resource.Error(null, it.exception?.message.toString()))
                            } }
                    } else {
                        _userLiveData.postValue(Resource.Error(null, "email already exist"))
                    } }
            } else -> {
            _userLiveData.postValue(Resource.Error(null, "No internet connection"))
        } }
        return userLiveData
    }

    fun saveUser(email: String,name:String,password: String,phone:String,token:String) {
        repository.saveUser(email, name,password,phone,token).addOnCompleteListener {
            if (it.isSuccessful) {
                _saveUserLiveData.postValue(Resource.Success(User(email,name,password,phone,token)))
            }else{
                _saveUserLiveData.postValue(Resource.Error(null,it.exception?.message.toString()))
            }
        }
    }

}