package com.example.seek_max.util

import com.example.seek_max.SeekMaxApplication

sealed class CommonUIEvent {
    object ShowMainProgress : CommonUIEvent()
    object HideMainProgress : CommonUIEvent()
    data class ShowSnackBarMessage(val message: AndroidUiMessage) : CommonUIEvent()
    data class ShowToast(val message: AndroidUiMessage) : CommonUIEvent()
}

data class AndroidUiMessage(
    val isError: Boolean = true,
    val message: String = "",
    val stringResId: Int? = null
) {
    fun getMessageText(): String {
        return if (stringResId != null)
            SeekMaxApplication.getResourceString(stringResId)
        else
            message
    }
}