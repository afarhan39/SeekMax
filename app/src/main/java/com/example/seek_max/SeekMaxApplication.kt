package com.example.seek_max

import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class SeekMaxApplication : Application() {
    companion object {
        lateinit var instance: SeekMaxApplication

        fun getContext(): Context {
            return instance.applicationContext
        }

        fun getResourceString(id: Int): String {
            return getContext().getString(id)
        }
    }

    init {
        instance = this
    }

    override fun onCreate() {
        super.onCreate()
    }
}