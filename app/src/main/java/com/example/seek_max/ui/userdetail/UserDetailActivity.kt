package com.example.seek_max.ui.userdetail

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import com.example.seek_max.base.BaseActivity
import com.example.seek_max.R
import com.example.seek_max.databinding.ActivityUserDetailBinding
import com.example.seek_max.ui.joblist.JobListActivity
import com.example.seek_max.common.CommonDetailsAdapter
import com.example.seek_max.common.CommonDetailsListItem
import com.example.seek_max.util.launchAndRepeatWithViewLifecycle
import com.example.seek_max.util.observeCommonUIEvents
import com.example.seek_max.util.setOnSingleClickListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserDetailActivity : BaseActivity() {

    private lateinit var binding: ActivityUserDetailBinding
    private val viewModel: UserDetailViewModel by viewModels()

    private var adapter: CommonDetailsAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViews()
        setListeners()
        observeData()
        viewModel.fetchInitialData()
    }

    private fun initViews() {
        adapter = CommonDetailsAdapter()
        binding.rvUserDetails.adapter = adapter
    }

    private fun setListeners() {
        binding.ivBack.setOnSingleClickListener {
            onBackPressed()
        }

        binding.bSignOut.setOnSingleClickListener {
            showLogoutConfirmation()
        }
    }

    private fun observeData() {
        viewModel.commonEvent.observeCommonUIEvents(this, binding.clRoot)

        launchAndRepeatWithViewLifecycle {
            viewModel.mainDataForUi.collect {
                    updateValueToUi(it)
            }
        }

        launchAndRepeatWithViewLifecycle {
            viewModel.mainUiEvent.collect {
                when (it) {
                    UserDetailUiEvent.NavToJobList -> {
                        startActivity(Intent(this@UserDetailActivity, JobListActivity::class.java).apply {
                            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        })
                    }
                }
            }
        }
    }

    private fun updateValueToUi(data: UserDetailUiData) {
        Log.d("", "$data")
        binding.clRoot.isVisible = data.isInitialized
        adapter?.submitList(data.commonDetailsListItem)
    }

    private fun showLogoutConfirmation() {
        val builder = AlertDialog.Builder(this)
            .setTitle(R.string.sign_out)
            .setMessage(R.string.are_you_sure_sign_out)
            .setPositiveButton(R.string.sign_out) { dialog, _ ->
                dialog.dismiss()
                viewModel.logout()
            }
            .setNegativeButton(R.string.cancel) { dialog, _ ->
                dialog.dismiss()
                // User cancelled the dialog
            }
        builder.create().show()
    }
}

data class UserDetailUiData(
    val isInitialized: Boolean = false,
    val commonDetailsListItem: List<CommonDetailsListItem> = listOf()
)

sealed class UserDetailUiEvent {
    object NavToJobList : UserDetailUiEvent()
}