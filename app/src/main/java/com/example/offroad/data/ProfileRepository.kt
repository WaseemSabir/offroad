package com.example.offroad.data

/**
 * Interface to the data layer.
 */
interface ProfileRepository {
    suspend fun updateProfile(
        imageUrl: String,
        fullName: String,
        email: String,
        birthdayDate: String,
        phoneNumber: String,
        subscribed: Boolean
    )

    suspend fun getProfile(): Profile
}
