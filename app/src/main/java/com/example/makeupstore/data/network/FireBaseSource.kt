package com.example.makeupstore.data.network

import com.example.makeupstore.models.Comment
import com.example.makeupstore.models.User
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FireBaseSource @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) {

    fun signUpUser(email: String, password: String, fullName: String) =
        firebaseAuth.createUserWithEmailAndPassword(email, password)


    fun signInUser(email: String, password: String) =
        firebaseAuth.signInWithEmailAndPassword(email, password)

    fun signInWithGoogle(acct: GoogleSignInAccount) = firebaseAuth.signInWithCredential(
        GoogleAuthProvider.getCredential(acct.idToken, null)
    )

    fun saveUser(email: String, name: String, password: String, phone: String, token: String) =
        firestore
            .collection("users").document(token)
            .set(
                User(
                    email = email,
                    name = name,
                    password = password,
                    phone = phone,
                    token = token
                )
            )

    fun fetchUser() = firestore.collection("users").get()
    fun sendComment(userName: String, comment: String, productId: Int):String {
        val data = hashMapOf<String, Any>(
            "comment" to comment,
            "time" to Timestamp.now(),
            "user" to userName,
            "productId" to productId,
            "id" to (userName+Timestamp.now())
        )

        firestore.collection("comments")
            .add(data)
       return "Comment posted successfully"

    }

    suspend fun getComments(productId: Int): List<Comment> {
        val comments = ArrayList<Comment>()
        val data = firestore.collection("comments")
            .whereEqualTo("productId", productId)
            .get()
            .await()
        data.documents.forEach {
            it.apply {
                comments.add(
                    Comment(
                        get("user") as String,
                        get("comment") as String,
                        get("time") as Timestamp,
                        get("id") as String
                    )
                )
            }
        }
        return comments
    }


    fun sendForgotPassword(email: String) = firebaseAuth.sendPasswordResetEmail(email)

}