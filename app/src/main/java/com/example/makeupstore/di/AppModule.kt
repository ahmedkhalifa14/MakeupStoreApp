package com.example.makeupstore.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.makeupstore.data.local.CartDao
import com.example.makeupstore.data.local.DataStoreManager
import com.example.makeupstore.data.local.FavDao
import com.example.makeupstore.data.local.RoomDatabaseClass
import com.example.makeupstore.data.network.FireBaseSource
import com.example.makeupstore.data.network.MakeupApiService
import com.example.makeupstore.qualifiers.IOThread
import com.example.makeupstore.qualifiers.MainThread
import com.example.makeupstore.repository.ApiRepo
import com.example.makeupstore.repository.ApiRepoImpl
import com.example.makeupstore.repository.FirebaseRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @MainThread
    @Singleton
    @Provides
    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main

    @IOThread
    @Singleton
    @Provides
    fun provideIODispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Singleton
    @Provides
    fun provideApplicationContext(
        @ApplicationContext context: Context
    ) = context

    @Provides
    @Singleton
    fun provideRoomDatabase(@ApplicationContext context: Context): RoomDatabaseClass {
        return Room.databaseBuilder(
            context,
            RoomDatabaseClass::class.java,
            "makeup_store_db"
        ).setJournalMode(RoomDatabase.JournalMode.TRUNCATE)
            .build()
    }

    @Provides
    @Singleton
    fun provideApiRepo(
        apiService: MakeupApiService,
        dispatcher: CoroutineDispatcher,
        cartDao: CartDao,
        favDao: FavDao
    ): ApiRepo {
        return ApiRepoImpl(apiService, dispatcher, cartDao, favDao)
    }

    @Provides
    @Singleton
    fun dataStoreManager(@ApplicationContext context: Context): DataStoreManager =
        DataStoreManager(context)
}