package com.example.offroad.di

import com.example.offroad.data.DefaultProfileRepository
import com.example.offroad.data.ProfileRepository
import com.example.offroad.data.source.network.NetworkDataSource
import com.example.offroad.data.source.network.ProfileNetworkDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Singleton
    @Binds
    abstract fun bindTaskRepository(repository: DefaultProfileRepository): ProfileRepository
}

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {

    @Singleton
    @Binds
    abstract fun bindNetworkDataSource(dataSource: ProfileNetworkDataSource): NetworkDataSource
}
