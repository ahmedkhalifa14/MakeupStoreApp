package com.example.makeupstore.models

import com.google.firebase.Timestamp

data class Comment (
    val userName:String?="user",
    val comment: String,
    val date:Timestamp,
    val id:String
        )
