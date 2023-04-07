package com.example.makeupstore.repository

import com.example.makeupstore.data.network.FireBaseSource
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import javax.inject.Inject

class AuthRepo @Inject constructor(private val fireBaseSource: FireBaseSource) {


    fun signUpUser(email: String, password: String, fullName: String) =
        fireBaseSource.signUpUser(email, password, fullName)

    fun signInUser(email: String, password: String) = fireBaseSource.signInUser(email, password)

    fun signInWithGoogle(acct: GoogleSignInAccount) = fireBaseSource.signInWithGoogle(acct)

    fun saveUser(email: String, name: String, password: String, phone: String, token: String) =
        fireBaseSource.saveUser(email, name, password, phone, token)

    fun fetchUser() = fireBaseSource.fetchUser()

    fun sendForgotPassword(email: String) = fireBaseSource.sendForgotPassword(email)

}