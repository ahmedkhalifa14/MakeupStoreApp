package com.example.makeupstore.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.makeupstore.models.User

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.io.IOException

class DataStoreManager(context: Context) {
    private val Context.isFirstTimeLaunchDataStore:
            DataStore<Preferences> by preferencesDataStore("IS_FIRST_TIME_LAUNCH_KEY")
    private val isFirstTimeLaunchDataStore = context.isFirstTimeLaunchDataStore
    private val Context.userInfoDataStore:
            DataStore<Preferences> by preferencesDataStore(name = "USER_INFO_KEY")
    private val userInfoDataStore = context.userInfoDataStore

    companion object {
        val isFirstTimeLaunchKey = booleanPreferencesKey("IS_FIRST_TIME_LAUNCH_KEY")
        val userTokenKey = stringPreferencesKey("USER_TOKEN_KEY")
        val userMailKey = stringPreferencesKey("USER_MAIL_KEY")
        val userNameKey = stringPreferencesKey("USER_NAME_KEY")
        val userPasswordKey = stringPreferencesKey("USER_PASSWORD_KEY")
        val userPhoneKey = stringPreferencesKey("USER_PHONE_KEY")
    }

    suspend fun setUserInfo(user: User) {
        userInfoDataStore.edit { preferences ->
            preferences[userMailKey] = user.email.toString()
            preferences[userNameKey] = user.name.toString()
            preferences[userPasswordKey] = user.password.toString()
            preferences[userPhoneKey] = user.password.toString()
            preferences[userTokenKey] = user.token.toString()

        }
    }

    fun getUserInfo(): Flow<User> {
        return userInfoDataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                User(
                    preferences[userMailKey] ?: "",
                    preferences[userNameKey] ?: "",
                    preferences[userPasswordKey] ?: "",
                    preferences[userPhoneKey] ?: "",
                    preferences[userTokenKey]
                )
            }
    }

    suspend fun setFirstTimeLaunch(isFirstTimeLaunch: Boolean) {
        isFirstTimeLaunchDataStore.edit { preferences ->
            preferences[isFirstTimeLaunchKey] = isFirstTimeLaunch
        }
    }

    fun isFirstTimeLaunch(): Flow<Boolean> {
        return isFirstTimeLaunchDataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                val isFirstTimeLaunch = preferences[isFirstTimeLaunchKey] ?: false
                isFirstTimeLaunch
            }
    }

}