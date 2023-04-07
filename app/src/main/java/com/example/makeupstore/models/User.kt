package com.example.makeupstore.models

data class User(
    val email: String? = "",
    val name: String? = "",
    val password: String? = "",
    val phone: String? = "",
    val token: String? = "",
)