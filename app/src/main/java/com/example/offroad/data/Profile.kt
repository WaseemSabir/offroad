package com.example.offroad.data

data class Profile(
    val imageUrl: String,
    val fullName: String,
    val email: String,
    val birthdayDate: String,
    val phoneNumber: String,
    val subscribed: Boolean
) {}
