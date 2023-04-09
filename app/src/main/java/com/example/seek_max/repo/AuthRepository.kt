package com.example.seek_max.repo

import android.util.Log
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.exception.ApolloException
import com.example.seek_max.AuthMutation
import com.example.seek_max.manager.SettingsManager
import com.example.seek_max.util.Resource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class AuthRepository constructor(
    private val apolloClient: ApolloClient,
    private val ioDispatcher: CoroutineDispatcher,
    private val settingsManager: SettingsManager
) {

    fun auth(username: String, password: String) = flow {
        try {
            val resp = apolloClient.mutation(AuthMutation(username, password)).execute()
            Log.d("resp", "data ${resp.data}")
            Log.d("resp", "errors ${resp.errors}")
            if (resp.data?.auth != null && resp.errors.isNullOrEmpty()) {
                settingsManager.jwtToken = resp.data?.auth
                settingsManager.userName = username
                emit(Resource.success(resp.data))
            }
            else
                emit(Resource.error(resp.errors))
        } catch (e: ApolloException) {
            emit(Resource.error(e))
        }

    }.flowOn(ioDispatcher)

    fun logout() = flow {
        settingsManager.jwtToken = null
        settingsManager.userName = null
        emit(Resource.success(true))
    }
}