package com.example.seek_max.okhttp

import com.example.seek_max.manager.SettingsManager
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(
    private val settingsManager: SettingsManager
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .apply {
                settingsManager.jwtToken?.let { token ->
                    addHeader("Authorization", token)
                }
            }
            .build()
        return chain.proceed(request)
    }
}