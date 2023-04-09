package com.example.seek_max.repo

import android.util.Log
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.Optional
import com.apollographql.apollo3.exception.ApolloException
import com.example.seek_max.ActiveJobsQuery
import com.example.seek_max.ApplyMutation
import com.example.seek_max.JobQuery
import com.example.seek_max.util.Resource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class JobRepository constructor(
    private val apolloClient: ApolloClient,
    private val ioDispatcher: CoroutineDispatcher
) {

    fun getActiveJobList(page: Int) = flow {
        try {
            val resp = apolloClient.query(ActiveJobsQuery(Optional.present(10), Optional.present(page))).execute()
            Log.d("resp", "data ${resp.data}")
            Log.d("resp", "errors ${resp.errors}")
            if (resp.data != null && resp.errors.isNullOrEmpty()) {
                emit(Resource.success(resp.data))
            }
            else
                emit(Resource.error(resp.errors))
        } catch (e: ApolloException) {
            emit(Resource.error(e))
        }

    }.flowOn(ioDispatcher)

    fun getJob(jobId: String) = flow {
        try {
            val resp = apolloClient.query(JobQuery(jobId)).execute()
            if (resp.data != null && resp.errors.isNullOrEmpty()) {
                emit(Resource.success(resp.data))
            }
            else
                emit(Resource.error(resp.errors))
        } catch (e: ApolloException) {
            emit(Resource.error(e))
        }

    }.flowOn(ioDispatcher)

    fun applyJob(jobId: String) = flow {
        try {
            val resp = apolloClient.mutation(ApplyMutation(jobId)).execute()
            if (resp.data != null && resp.errors.isNullOrEmpty()) {
                emit(Resource.success(resp.data))
            }
            else
                emit(Resource.error(resp.errors))
        } catch (e: ApolloException) {
            emit(Resource.error(e))
        }

    }.flowOn(ioDispatcher)
}