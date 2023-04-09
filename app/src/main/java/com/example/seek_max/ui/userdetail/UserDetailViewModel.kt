package com.example.seek_max.ui.userdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.seek_max.R
import com.example.seek_max.hilt.MainDispatcher
import com.example.seek_max.manager.SettingsManager
import com.example.seek_max.repo.AuthRepository
import com.example.seek_max.common.CommonDetailsListItem
import com.example.seek_max.util.AndroidUiMessage
import com.example.seek_max.util.CommonUIEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class UserDetailViewModel @Inject constructor(
    @MainDispatcher private var mainDispatcher: CoroutineDispatcher,
    private val authRepository: AuthRepository,
    private val settingsManager: SettingsManager
) : ViewModel() {

    private val _commonEvent = MutableSharedFlow<CommonUIEvent>(replay = 0)
    val commonEvent: SharedFlow<CommonUIEvent> = _commonEvent
    private var pendingErrorEvent: CommonUIEvent? = null

    private val _mainUiEvent = MutableSharedFlow<UserDetailUiEvent>(replay = 0)
    val mainUiEvent: SharedFlow<UserDetailUiEvent> = _mainUiEvent

    private val _mainDataForUi = MutableStateFlow(UserDetailUiData())
    val mainDataForUi: StateFlow<UserDetailUiData> = _mainDataForUi

    private suspend fun updateValuesToUi() {
        _mainDataForUi.emit(
            UserDetailUiData(
                isInitialized = true,
                commonDetailsListItem = generateUserDetailsListItem()
            )
        )
    }

    fun fetchInitialData() {
        viewModelScope.launch(mainDispatcher) {
            _commonEvent.emit(CommonUIEvent.ShowMainProgress)
            updateValuesToUi()
            _commonEvent.emit(CommonUIEvent.HideMainProgress)
        }
    }

    fun logout() {
        viewModelScope.launch(mainDispatcher) {
            awaitAll(
                async {
                    _commonEvent.emit(CommonUIEvent.ShowMainProgress)
                    authRepository.logout().collect {
                        if (it.isSuccess()) {
                            _commonEvent.emit(CommonUIEvent.ShowToast(
                                AndroidUiMessage(
                                    stringResId = R.string.sign_out_successfully
                                )
                            ))
                            _mainUiEvent.emit(UserDetailUiEvent.NavToJobList)
                        }
                    }
                }
            )

            updateValuesToUi()
            _commonEvent.emit(CommonUIEvent.HideMainProgress)
            handlePendingErrorEvent()
        }
    }

    private fun generateUserDetailsListItem(): List<CommonDetailsListItem> {
        val username: String = settingsManager.userName ?: return emptyList()
        return buildList {
            add(CommonDetailsListItem("Username", username))
            add(CommonDetailsListItem("Email", "$username@gmail.com"))
            add(CommonDetailsListItem("Jobs Applied", (0..10).random().toString()))
        }
    }

    private suspend fun handlePendingErrorEvent() {
        pendingErrorEvent?.let {
            _commonEvent.emit(it)
            pendingErrorEvent = null
        }
    }
}