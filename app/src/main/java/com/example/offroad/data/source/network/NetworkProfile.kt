package com.example.offroad.data.source.network

/**
 * See ModelMappingExt.kt for mapping functions used to
 * convert this model to other models.
 */
data class NetworkProfile(
    val imageUrl: String,
    val fullName: String,
    val email: String,
    val birthdayDate: String,
    val phoneNumber: String,
    val subscribed: Boolean
)

