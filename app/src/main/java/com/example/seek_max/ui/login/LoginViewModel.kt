package com.example.seek_max.ui.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.seek_max.hilt.MainDispatcher
import com.example.seek_max.repo.AuthRepository
import com.example.seek_max.util.CommonUIEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class LoginViewModel @Inject constructor(
    @MainDispatcher private var mainDispatcher: CoroutineDispatcher,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _commonEvent = MutableSharedFlow<CommonUIEvent>(replay = 0)
    val commonEvent: SharedFlow<CommonUIEvent> = _commonEvent
    private var pendingErrorEvent: CommonUIEvent? = null

    private val _mainUiEvent = MutableSharedFlow<LoginUiEvent>(replay = 0)
    val mainUiEvent: SharedFlow<LoginUiEvent> = _mainUiEvent

    private val _mainDataForUi = MutableStateFlow(LoginUiData())
    val mainDataForUi: StateFlow<LoginUiData> = _mainDataForUi

    private var _userName: String? = null
    private var _password: String? = null

    private suspend fun updateValuesToUi() {
        _mainDataForUi.emit(
            LoginUiData(
                isInitialized = true,
                isSignInEnabled = !_userName.isNullOrEmpty() && !_password.isNullOrEmpty()
            )
        )
    }

    fun login() {
        viewModelScope.launch(mainDispatcher) {
            awaitAll(
                async {
                    _commonEvent.emit(CommonUIEvent.ShowMainProgress)
                    val username: String = _userName ?: return@async
                    val password: String = _password ?: return@async
                    authRepository.auth(username, password).collect {
                        if (it.isSuccess()) {
                            // save in settings manager?
                            _mainUiEvent.emit(LoginUiEvent.NavToJobList)
                        }
                        else if (it.isError()) {
                            pendingErrorEvent = it.errorUiEvent()
                            Log.d("JobListViewModel", "are you here")
                            _commonEvent.emit(it.errorUiEvent())
                        }
                    }
                }
            )

            updateValuesToUi()
            _commonEvent.emit(CommonUIEvent.HideMainProgress)
            handlePendingErrorEvent()
        }
    }

    fun updateUserName(value: String) {
        viewModelScope.launch(mainDispatcher) {
            _userName = value
            updateValuesToUi()
        }
    }

    fun updatePassword(value: String) {
        viewModelScope.launch(mainDispatcher) {
            _password = value
            updateValuesToUi()
        }
    }

    private suspend fun handlePendingErrorEvent() {
        pendingErrorEvent?.let {
            _commonEvent.emit(it)
            pendingErrorEvent = null
        }
    }
}