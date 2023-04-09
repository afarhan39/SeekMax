package com.example.seek_max.hilt

import com.apollographql.apollo3.ApolloClient
import com.example.seek_max.manager.SettingsManager
import com.example.seek_max.repo.AuthRepository
import com.example.seek_max.repo.JobRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher

@Module
@InstallIn(SingletonComponent::class)
class RepoModule {

    @Provides
    fun provideAuthRepository(
        @DefaultApolloClient apolloClient: ApolloClient,
        @IoDispatcher ioDispatcher: CoroutineDispatcher,
        settingsManager: SettingsManager
    ) = AuthRepository(apolloClient, ioDispatcher, settingsManager)

    @Provides
    fun provideJobRepository(
        @DefaultApolloClient apolloClient: ApolloClient,
        @IoDispatcher ioDispatcher: CoroutineDispatcher
    ) = JobRepository(apolloClient, ioDispatcher)
}