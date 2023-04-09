package com.example.seek_max.ui.joblist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.seek_max.ActiveJobsQuery
import com.example.seek_max.hilt.MainDispatcher
import com.example.seek_max.manager.SettingsManager
import com.example.seek_max.repo.JobRepository
import com.example.seek_max.util.CommonUIEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class JobListViewModel @Inject constructor(
    @MainDispatcher private var mainDispatcher: CoroutineDispatcher,
    private val jobRepository: JobRepository,
    private val settingsManager: SettingsManager
) : ViewModel() {

    private val _commonEvent = MutableSharedFlow<CommonUIEvent>(replay = 0)
    val commonEvent: SharedFlow<CommonUIEvent> = _commonEvent
    private var pendingErrorEvent: CommonUIEvent? = null

    private val _mainDataForUi = MutableStateFlow(JobListUiData())
    val mainDataForUi: StateFlow<JobListUiData> = _mainDataForUi

    private var _activeJobsQuery: ActiveJobsQuery.Data? = null
    private var _jobList: MutableList<ActiveJobsQuery.Job> = mutableListOf()

    var hasNext: Boolean = false
    var isLoading: Boolean = false
    private var _page: Int = 1

    private suspend fun updateValuesToUi() {
        val temp = _activeJobsQuery?.active?.jobs?.filterNotNull() ?: listOf()
        if (temp.isNotEmpty())
            _jobList += temp

        _mainDataForUi.emit(
            JobListUiData(
                isInitialized = true,
                jobList = _jobList.toList(),
                userName = settingsManager.userName,
                isUserSignedIn = settingsManager.jwtToken != null
            )
        )
    }

    fun loadNextPage() {
        _page += 1
        getActiveJobList(false)
    }

    fun getActiveJobList(isShowProgress: Boolean = true) {
        viewModelScope.launch(mainDispatcher) {
            awaitAll(
                async {
                    if (isShowProgress)
                        _commonEvent.emit(CommonUIEvent.ShowMainProgress)
                    isLoading = true
                    jobRepository.getActiveJobList(_page).collect {
                        if (it.isSuccess()) {
                            _activeJobsQuery = it.data
                            hasNext = it.data?.active?.hasNext == true
                        }
                        else if (it.isError()) {
                            pendingErrorEvent = it.errorUiEvent()
                        }
                    }
                }
            )

            updateValuesToUi()
            _commonEvent.emit(CommonUIEvent.HideMainProgress)
            isLoading = false
            handlePendingErrorEvent()
        }
    }

    private suspend fun handlePendingErrorEvent() {
        pendingErrorEvent?.let {
            _commonEvent.emit(it)
            pendingErrorEvent = null
        }
    }
}