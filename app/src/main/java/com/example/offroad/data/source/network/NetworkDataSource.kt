package com.example.offroad.data.source.network

/**
 * Main entry point for accessing tasks data from the network.
 */
interface NetworkDataSource {
    suspend fun loadProfile(): NetworkProfile
    suspend fun saveProfile(profile: NetworkProfile)
}
