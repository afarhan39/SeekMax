package com.example.seek_max.manager

import android.content.SharedPreferences

private const val KEY_JWT_TOKEN = "KEY_JWT_TOKEN"
private const val KEY_USER_NAME = "KEY_USER_NAME"

class SettingsManager(
    private val preferences: SharedPreferences
) {
    var jwtToken: String?
        get() {
            return preferences.getString(KEY_JWT_TOKEN, null)
        }
        set(value) {
            preferences.edit()
                .putString(KEY_JWT_TOKEN, value)
                .apply()
        }

    var userName: String?
        get() {
            return preferences.getString(KEY_USER_NAME, null)
        }
        set(value) {
            preferences.edit()
                .putString(KEY_USER_NAME, value)
                .apply()
        }
}