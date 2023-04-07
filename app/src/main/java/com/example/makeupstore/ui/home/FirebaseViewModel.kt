package com.example.makeupstore.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.makeupstore.models.Comment
import com.example.makeupstore.qualifiers.MainThread
import com.example.makeupstore.repository.FirebaseRepo
import com.example.makeupstore.utils.Event
import com.example.makeupstore.utils.Resource
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class FirebaseViewModel @Inject constructor(
    private val repository: FirebaseRepo,
    private val fireStore: FirebaseFirestore,
    @MainThread
    private val dispatcher: CoroutineDispatcher,
) : ViewModel() {


    private val _sendCommentStatus =
        MutableStateFlow<Event<Resource<String>>>(Event(Resource.Init()))
    val sendCommentStatus: MutableStateFlow<Event<Resource<String>>> =
        _sendCommentStatus

    private val _getCommentStatus =
        MutableStateFlow<Event<Resource<List<Comment>>>>(Event(Resource.Init()))
    val getCommentStatus: MutableStateFlow<Event<Resource<List<Comment>>>> =
        _getCommentStatus

    fun sendComment(userName: String, comment: String, productId: Int) =
        viewModelScope.launch(dispatcher) {
            _sendCommentStatus.emit(Event(Resource.Loading()))
            val result = repository.sendComment(userName, comment, productId)
            _sendCommentStatus.emit(Event(result))
        }


    fun getComments(productId: Int) =
        viewModelScope.launch(dispatcher) {
            _getCommentStatus.emit(Event(Resource.Loading()))
            val result = repository.getComments(productId)
            _getCommentStatus.emit(Event(result))
        }

}