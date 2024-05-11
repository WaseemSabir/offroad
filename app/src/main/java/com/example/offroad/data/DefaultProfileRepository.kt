package com.example.offroad.data

import com.example.offroad.data.source.network.NetworkDataSource
import com.example.offroad.di.ApplicationScope
import com.example.offroad.di.DefaultDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DefaultProfileRepository @Inject constructor(
    private val networkDataSource: NetworkDataSource,
    @DefaultDispatcher private val dispatcher: CoroutineDispatcher,
    @ApplicationScope private val scope: CoroutineScope,
) : ProfileRepository {

    override suspend fun updateProfile(
        imageUrl: String,
        fullName: String,
        email: String,
        birthdayDate: String,
        phoneNumber: String,
        subscribed: Boolean
    ) {
        val profile = Profile(
            imageUrl = imageUrl,
            fullName = fullName,
            email = email,
            birthdayDate = birthdayDate,
            phoneNumber = phoneNumber,
            subscribed = subscribed
        )
        scope.launch {
            try {
                val networkProfile = withContext(dispatcher) {
                    profile.toNetwork()
                }
                networkDataSource.saveProfile(networkProfile)
            } catch (_: Exception) {
            }
        }
    }

    override suspend fun getProfile(): Profile {
        return withContext(dispatcher) {
            networkDataSource.loadProfile().toExternal()
        }
    }
}
