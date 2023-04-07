package com.example.makeupstore.utils

import android.view.View
import com.google.android.material.snackbar.Snackbar

object Utils {
    inline fun <T> safeCall(action: () -> Resource<T>): Resource<T> {
        return try {
            action()
        } catch (e: Exception) {
            Resource.Error(null,e.message ?: "An unknown error occurred")
        }
    }
    fun View.showSnackBar(message:String){
        Snackbar.make(this,message, Snackbar.LENGTH_LONG).show()
    }
    fun getTimeStamp()=System.currentTimeMillis() / 1000
}