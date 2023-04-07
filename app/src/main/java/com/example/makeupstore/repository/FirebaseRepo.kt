package com.example.makeupstore.repository

import com.example.makeupstore.data.network.FireBaseSource
import com.example.makeupstore.models.Comment
import com.example.makeupstore.utils.Resource
import com.example.makeupstore.utils.Utils.safeCall
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.coroutineContext
import kotlinx.coroutines.CoroutineDispatcher
import com.example.makeupstore.qualifiers.IOThread
import kotlinx.coroutines.Dispatchers

class FirebaseRepo @Inject constructor(
    private val fireBaseSource: FireBaseSource,
//    @IOThread
//    private val dispatcher: CoroutineDispatcher
) {
    suspend fun sendComment(userName: String, comment: String, productId: Int) :Resource<String> =
        withContext(Dispatchers.IO) {
            safeCall {
                val result = fireBaseSource.sendComment(userName, comment, productId)
                Resource.Success(result)
            }
        }

    suspend fun getComments(productId: Int):Resource<List<Comment>> =
        withContext(Dispatchers.IO) {
            safeCall{
                val result = fireBaseSource.getComments(productId)
                Resource.Success(result)
            }

        }


}