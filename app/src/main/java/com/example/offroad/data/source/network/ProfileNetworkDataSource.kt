package com.example.offroad.data.source.network

import javax.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class ProfileNetworkDataSource @Inject constructor() : NetworkDataSource {
    private val accessMutex = Mutex()
    private var profile = NetworkProfile(
        imageUrl = "https://fastly.picsum.photos/id/10/200/300.jpg",
        fullName = "Waseem Sabir",
        birthdayDate = "2000-06-03",
        email = "waseemsabir99@gmail.com",
        phoneNumber = "+923151156034",
        subscribed = true
    )

    override suspend fun loadProfile(): NetworkProfile = accessMutex.withLock {
        delay(SERVICE_LATENCY_IN_MILLIS)
        return profile
    }

    override suspend fun saveProfile(profile: NetworkProfile) = accessMutex.withLock {
        delay(SERVICE_LATENCY_IN_MILLIS)
        this.profile = profile
    }
}

private const val SERVICE_LATENCY_IN_MILLIS = 2000L
