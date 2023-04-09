package com.example.seek_max.util

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

inline fun <reified T> String?.fromJsonToObj(): T? {
    if (this == null) {
        return null
    }
    return tryOrNull {
        Gson().fromJson(this, object : TypeToken<T>() {}.type)
    }
}

inline fun <reified T> T?.toJsonString(): String? {
    if (this == null) {
        return null
    }
    return Gson().toJson(this, object : TypeToken<T>() {}.type)
}