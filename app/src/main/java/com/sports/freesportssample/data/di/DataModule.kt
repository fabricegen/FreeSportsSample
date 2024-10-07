package com.sports.freesportssample.data.di

import com.sports.freesportssample.data.api.NetworkModule
import com.sports.freesportssample.data.common.CoroutineDispatchers
import com.sports.freesportssample.data.common.DefaultCoroutineDispatchers
import com.sports.freesportssample.data.common.DefaultDispatcher
import com.sports.freesportssample.data.common.IoDispatcher
import com.sports.freesportssample.data.common.MainDispatcher
import com.sports.freesportssample.data.repository.DefaultLeaguesRepository
import com.sports.freesportssample.domain.repository.LeaguesRepository
import com.sports.freesportssample.data.repository.RemoteLeaguesDataSource
import com.sports.freesportssample.data.repository.DefaultRemoteLeaguesDataSource
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module(includes = [NetworkModule::class])
@InstallIn(SingletonComponent::class)
interface DataModule {
    @Binds
    @Singleton
    fun bindsCollectionRepository(impl: DefaultLeaguesRepository): LeaguesRepository

    @Binds
    @Singleton
    fun bindsRemoteDataSource(impl: DefaultRemoteLeaguesDataSource): RemoteLeaguesDataSource

    @Binds
    @Singleton
    fun bindsCoroutineDispatchers(impl: DefaultCoroutineDispatchers): CoroutineDispatchers

    @Module
    @InstallIn(SingletonComponent::class)
    object Provider {
        @Provides
        @Singleton
        @MainDispatcher
        fun mainDispatcher(): CoroutineDispatcher = Dispatchers.Main

        @Provides
        @Singleton
        @IoDispatcher
        fun ioDispatcher(): CoroutineDispatcher = Dispatchers.IO

        @Provides
        @Singleton
        @DefaultDispatcher
        fun defaultDispatcher(): CoroutineDispatcher = Dispatchers.Default
    }
}
