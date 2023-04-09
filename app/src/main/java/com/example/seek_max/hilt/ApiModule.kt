package com.example.seek_max.hilt

import android.content.Context
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.network.okHttpClient
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.example.seek_max.manager.SettingsManager
import com.example.seek_max.okhttp.AuthInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class APIModule {

    @Singleton
    @Provides
    @DefaultApolloClient
    fun provideApolloClient(
        @DefaultOkHttpClient client: OkHttpClient
    ): ApolloClient {
        return ApolloClient.Builder()
            .okHttpClient(client)
            .serverUrl("http://192.168.1.19:3002")
            .build()
    }

    @Singleton
    @Provides
    @DefaultOkHttpClient
    fun providesOkHttpClient(
        chuckerInterceptor: ChuckerInterceptor,
        authInterceptor: AuthInterceptor
    ): OkHttpClient {
        return OkHttpClient().newBuilder()
            .addInterceptor(chuckerInterceptor)
            .addInterceptor(authInterceptor)
            .build()
    }

    @Singleton
    @Provides
    fun provideChuckerInterceptor(@ApplicationContext context: Context) =
        ChuckerInterceptor.Builder(context).build()

   @Singleton
    @Provides
    fun provideAuthInterceptor(settingsManager: SettingsManager) =
        AuthInterceptor(settingsManager)
}

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class DefaultApolloClient


@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class DefaultOkHttpClient