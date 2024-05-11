package com.example.offroad.data

import com.example.offroad.data.source.network.NetworkProfile

/**
 * Data model mapping extension functions. There are two model types:
 *
 * - Profile: External model exposed to other layers in the architecture.
 * Obtained using `toExternal`.
 *
 * - NetworkProfile: Internal model used to represent a task from the network. Obtained using
 * `toNetwork`.
 */
fun NetworkProfile.toExternal() = Profile(
    imageUrl = imageUrl,
    fullName = fullName,
    email = email,
    birthdayDate = birthdayDate,
    phoneNumber = phoneNumber,
    subscribed = subscribed
)

fun Profile.toNetwork() = NetworkProfile(
    imageUrl = imageUrl,
    fullName = fullName,
    email = email,
    birthdayDate = birthdayDate,
    phoneNumber = phoneNumber,
    subscribed = subscribed
)
