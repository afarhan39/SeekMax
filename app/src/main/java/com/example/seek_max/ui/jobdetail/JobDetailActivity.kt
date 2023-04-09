package com.example.seek_max.ui.jobdetail

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.view.isVisible
import com.example.seek_max.R
import com.example.seek_max.base.BaseActivity
import com.example.seek_max.common.CommonDetailsAdapter
import com.example.seek_max.common.CommonDetailsListItem
import com.example.seek_max.databinding.ActivityJobDetailBinding
import com.example.seek_max.ui.login.LoginActivity
import com.example.seek_max.util.launchAndRepeatWithViewLifecycle
import com.example.seek_max.util.observeCommonUIEvents
import com.example.seek_max.util.setOnSingleClickListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class JobDetailActivity : BaseActivity() {

    companion object {
        const val KEY_JOB_ID = "KEY_JOB_ID"
    }

    private lateinit var binding: ActivityJobDetailBinding
    private val viewModel: JobDetailViewModel by viewModels()

    private var adapter: CommonDetailsAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJobDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViews()
        setListeners()
        observeData()
        viewModel.fetchInitialData(intent.extras?.getString(KEY_JOB_ID))
    }

    private fun initViews() {
        adapter = CommonDetailsAdapter()
        binding.rvJobDetails.adapter = adapter
    }

    private fun setListeners() {
        binding.ivBack.setOnSingleClickListener {
            onBackPressed()
        }

        binding.bAction.setOnSingleClickListener {
            if (viewModel.mainDataForUi.value.isUserSignedIn) {
                viewModel.applyJob()
            } else {
                startActivity(Intent(this, LoginActivity::class.java))
            }
        }
    }

    private fun observeData() {
        viewModel.commonEvent.observeCommonUIEvents(this, binding.clRoot)

        launchAndRepeatWithViewLifecycle {
            viewModel.mainDataForUi.collect {
                updateValueToUi(it)
            }
        }
    }

    private fun updateValueToUi(data: JobDetailUiData) {
        binding.clRoot.isVisible = data.isInitialized
        adapter?.submitList(data.jobDetailsListItem)
        when {
            !data.isUserSignedIn -> {
                binding.bAction.text = getString(R.string.sign_in)
                binding.bAction.isEnabled = true
            }
            data.isJobApplied -> {
                binding.bAction.text = getString(R.string.job_applied)
                binding.bAction.isEnabled = false
            }
            else -> {
                binding.bAction.text = getString(R.string.apply_now)
                binding.bAction.isEnabled = true
            }
        }
    }
}

data class JobDetailUiData(
    val isInitialized: Boolean = false,
    val jobDetailsListItem: List<CommonDetailsListItem> = listOf(),
    val isUserSignedIn: Boolean = false,
    val isJobApplied: Boolean = false
)