package com.example.makeupstore.di


import com.example.makeupstore.data.local.RoomDatabaseClass
import com.example.makeupstore.data.network.FireBaseSource
import com.example.makeupstore.data.network.MakeupApiService
import com.example.makeupstore.repository.FirebaseRepo
import com.example.makeupstore.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.CoroutineDispatcher
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(ViewModelComponent::class)
object ViewModel {

    @Provides
    @ViewModelScoped
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        return logging
    }

    @Provides
    @ViewModelScoped
    fun provideOkHttpClient(interceptor: HttpLoggingInterceptor): OkHttpClient =
        OkHttpClient.Builder().addInterceptor(interceptor).build()

    @Provides
    @ViewModelScoped
    fun provideCartDao(roomDB: RoomDatabaseClass) = roomDB.cartDao()

    @Provides
    @ViewModelScoped
    fun provideFavDao(roomDB: RoomDatabaseClass) = roomDB.favDao()

    @Provides
    @ViewModelScoped
    fun provideApiService(okHttpClient: OkHttpClient): MakeupApiService =
        Retrofit.Builder()
            .run {
                baseUrl(Constants.BASE_URL)
                client(okHttpClient)
                addConverterFactory(GsonConverterFactory.create())
                build()
            }.create(MakeupApiService::class.java)



}