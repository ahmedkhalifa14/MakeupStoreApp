package com.example.makeupstore.di

import android.content.Context
import com.example.makeupstore.R
import com.example.makeupstore.data.network.FireBaseSource
import com.example.makeupstore.repository.FirebaseRepo
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import com.google.android.gms.auth.api.signin.GoogleSignIn
import kotlinx.coroutines.CoroutineDispatcher


@Module
@InstallIn(SingletonComponent::class)
object FireBaseModule {

    @Provides
    @Singleton
    fun provideFirebaseRepo(
        fbs: FireBaseSource,
       // dispatcher: CoroutineDispatcher
    ): FirebaseRepo {
        return FirebaseRepo(fbs)
    }
    @Provides
    @Singleton
    fun provideFireBaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }
    @Provides
    @Singleton
    fun provideFireBaseSource(auth:FirebaseAuth,fireStore:FirebaseFirestore)
    :FireBaseSource{
        return FireBaseSource(auth,fireStore)
    }

    @Provides
    @Singleton
    fun provideGso(@ApplicationContext context: Context) = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(context.getString(R.string.default_web_client_id))
        .requestEmail()
        .build()

    @Provides
    @Singleton
    fun provideGoogleClient(@ApplicationContext context: Context, gso:GoogleSignInOptions)= GoogleSignIn.getClient(context, gso)

    @Provides
    @Singleton
    fun provideFireStore()= FirebaseFirestore.getInstance()
}