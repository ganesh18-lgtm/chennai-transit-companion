package com.urbantransit.offline.di

import com.urbantransit.offline.data.remote.TransitRemoteDataSource
import com.urbantransit.offline.data.remote.TransitRemoteDataSourceImpl
import com.urbantransit.offline.data.repository.OfflineFirstTransitRepository
import com.urbantransit.offline.domain.repository.TransitRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindTransitRepository(
        impl: OfflineFirstTransitRepository
    ): TransitRepository

    @Binds
    @Singleton
    abstract fun bindTransitRemoteDataSource(
        impl: TransitRemoteDataSourceImpl
    ): TransitRemoteDataSource
}
