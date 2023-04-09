package com.example.seek_max.util

import androidx.annotation.StringRes
import com.example.seek_max.R
import com.example.seek_max.SeekMaxApplication
import java.io.IOException

data class Resource<out T>(
    val status: Status = Status.LOADING,
    val code: Int? = null,
    val data: T? = null,
    val message: String? = SeekMaxApplication.getResourceString(R.string.common_error_message)
) {
    companion object {
        private fun <T> resource(
            status: Status,
            code: Int?,
            data: T?,
            message: String?
        ): Resource<T> {
            return Resource(
                status = status,
                code = code,
                data = data,
                message = message
            )
        }

        fun <T> success(data: T?, message: String? = null): Resource<T> {
            return resource(Status.SUCCESS, 200, data, message)
        }

        fun <T> error(exception: Throwable? = null): Resource<T> {
            val errorMessage =
                exception?.message ?: SeekMaxApplication.getResourceString(R.string.common_error_message)
            return resource(Status.ERROR, 400, null, message = errorMessage)
        }

        fun <T> error(errors: List<com.apollographql.apollo3.api.Error>?): Resource<T> {
            val extensions = errors?.firstOrNull()?.extensions?.get("response") as LinkedHashMap<*, *>
            val extensionsErrorMsg = extensions["body"].toString()
            val errorMessage =
                if (extensionsErrorMsg == "null" || extensionsErrorMsg.isEmpty()) {
                    errors.firstOrNull()?.message
                        ?: SeekMaxApplication.getResourceString(R.string.common_error_message)
                } else {
                    extensionsErrorMsg
                }

            return resource(Status.ERROR, 400, null, message = errorMessage)
        }
    }

    fun isSuccess(): Boolean {
        return status == Status.SUCCESS
    }

    fun isError(): Boolean {
        return status == Status.ERROR
    }

    fun errorUiEvent(@StringRes customErrorStrId: Int? = null): CommonUIEvent {
        return CommonUIEvent.ShowSnackBarMessage(
            AndroidUiMessage(
                isError = true,
                message = message ?: "",
                stringResId = customErrorStrId
            )
        )
    }
}

enum class Status {
    SUCCESS, ERROR, LOADING
}

internal class NoDataException : IOException() {
    override val message: String
        get() = ""
}