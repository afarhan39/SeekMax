package com.example.seek_max.ui.jobdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.seek_max.JobQuery
import com.example.seek_max.R
import com.example.seek_max.common.CommonDetailsListItem
import com.example.seek_max.hilt.MainDispatcher
import com.example.seek_max.manager.SettingsManager
import com.example.seek_max.repo.JobRepository
import com.example.seek_max.util.AndroidUiMessage
import com.example.seek_max.util.CommonUIEvent
import com.example.seek_max.util.toRinggitWithDecimal
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class JobDetailViewModel @Inject constructor(
    @MainDispatcher private var mainDispatcher: CoroutineDispatcher,
    private val jobRepository: JobRepository,
    private val settingsManager: SettingsManager
) : ViewModel() {

    private val _commonEvent = MutableSharedFlow<CommonUIEvent>(replay = 0)
    val commonEvent: SharedFlow<CommonUIEvent> = _commonEvent
    private var pendingErrorEvent: CommonUIEvent? = null

    private val _mainDataForUi = MutableStateFlow(JobDetailUiData())
    val mainDataForUi: StateFlow<JobDetailUiData> = _mainDataForUi

    private var _jobQuery: JobQuery.Data? = null
    private var _jobId: String? = null

    private suspend fun updateValuesToUi() {
        _mainDataForUi.emit(
            JobDetailUiData(
                isInitialized = true,
                jobDetailsListItem = generateJobDetailsListItem(),
                isUserSignedIn = settingsManager.jwtToken != null,
                isJobApplied = _jobQuery?.job?.haveIApplied == true
            )
        )
    }

    fun fetchInitialData(jobIdExtra: String? = null) {
        viewModelScope.launch(mainDispatcher) {
            _jobId = jobIdExtra
            awaitAll(
                async {
                    val jobId: String = _jobId ?: return@async
                    _commonEvent.emit(CommonUIEvent.ShowMainProgress)
                    jobRepository.getJob(jobId).collect {
                        if (it.isSuccess())
                            _jobQuery = it.data
                        else if (it.isError())
                            pendingErrorEvent = it.errorUiEvent()
                    }
                }
            )
            updateValuesToUi()
            _commonEvent.emit(CommonUIEvent.HideMainProgress)
            handlePendingErrorEvent()
        }
    }

    // apply

    fun applyJob() {
        viewModelScope.launch(mainDispatcher) {
            awaitAll(
                async {
                    val jobId: String = _jobId ?: return@async
                    _commonEvent.emit(CommonUIEvent.ShowMainProgress)
                    jobRepository.applyJob(jobId).collect {
                        if (it.isSuccess()) {
                            val successfullyApplied = it.data?.apply == true
                            _commonEvent.emit(
                                CommonUIEvent.ShowSnackBarMessage(
                                    AndroidUiMessage(
                                        isError = successfullyApplied,
                                        stringResId = if (successfullyApplied)
                                            R.string.job_applied
                                        else
                                            R.string.better_luck_next_time
                                    )
                                )
                            )
                        } else if (it.isError())
                            pendingErrorEvent = it.errorUiEvent()
                    }
                }
            )
            updateValuesToUi()
            _commonEvent.emit(CommonUIEvent.HideMainProgress)
            handlePendingErrorEvent()
        }
    }

    private fun generateJobDetailsListItem(): List<CommonDetailsListItem> {
        val data = _jobQuery?.job ?: return emptyList()
        return buildList {
            data._id?.let {
                add(CommonDetailsListItem("Job ID", it))
            }
            data.positionTitle?.let {
                add(CommonDetailsListItem("Job Title", it))
            }
            data.description?.let {
                add(CommonDetailsListItem("Job Description", it))
            }
            data.salaryRange?.let {
                val minSalaryText = it.min.toRinggitWithDecimal()
                val maxSalaryText = it.max.toRinggitWithDecimal()
                val salaryRangeText = when {
                    minSalaryText.isNotEmpty() && maxSalaryText.isNotEmpty() -> "$minSalaryText - $maxSalaryText"
                    minSalaryText.isNotEmpty() -> "Minimum $minSalaryText"
                    maxSalaryText.isNotEmpty() -> "Maximum $maxSalaryText"
                    else -> return@let
                }
                add(CommonDetailsListItem("Salary Range", salaryRangeText))
            }
            data.location?.let {
                val locationText = when (it) {
                    0 -> "Australia"
                    1 -> "Malaysia"
                    else -> return@let
                }
                add(CommonDetailsListItem("Location", locationText))
            }
        }
    }

    private suspend fun handlePendingErrorEvent() {
        pendingErrorEvent?.let {
            _commonEvent.emit(it)
            pendingErrorEvent = null
        }
    }
}