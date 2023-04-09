package com.example.seek_max.ui.joblist

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.seek_max.ActiveJobsQuery
import com.example.seek_max.R
import com.example.seek_max.base.BaseActivity
import com.example.seek_max.databinding.ActivityJobListBinding
import com.example.seek_max.ui.jobdetail.JobDetailActivity
import com.example.seek_max.ui.joblist.adapter.JobListAdapter
import com.example.seek_max.ui.login.LoginActivity
import com.example.seek_max.ui.userdetail.UserDetailActivity
import com.example.seek_max.util.PaginationScrollListener
import com.example.seek_max.util.launchAndRepeatWithViewLifecycle
import com.example.seek_max.util.observeCommonUIEvents
import com.example.seek_max.util.setOnSingleClickListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class JobListActivity : BaseActivity() {

    private lateinit var binding: ActivityJobListBinding
    private val viewModel: JobListViewModel by viewModels()

    private var adapter: JobListAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJobListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViews()
        setListeners()
        observeData()
        viewModel.getActiveJobList()
    }

    private fun initViews() {
        adapter = JobListAdapter(
            onClickJob = {
                startActivity(Intent(this, JobDetailActivity::class.java).apply {
                    putExtra(JobDetailActivity.KEY_JOB_ID, it)
                }
                )
            }
        )
        binding.rvJobList.adapter = adapter
    }

    private fun setListeners() {
        binding.incUserHeader.tvUserName.setOnSingleClickListener {
            if (viewModel.mainDataForUi.value.isUserSignedIn) {
                startActivity(Intent(this, UserDetailActivity::class.java))
            } else {
                startActivity(Intent(this, LoginActivity::class.java))
            }

        }

        binding.rvJobList.addOnScrollListener(object :
            PaginationScrollListener(binding.rvJobList.layoutManager as LinearLayoutManager) {
            override fun loadMoreItems() {
                viewModel.loadNextPage()
            }

            override fun hasNext() = viewModel.hasNext

            override fun isLoading() = viewModel.isLoading
        })
    }

    private fun observeData() {
        viewModel.commonEvent.observeCommonUIEvents(this, binding.clRoot)

        launchAndRepeatWithViewLifecycle {
            viewModel.mainDataForUi.collect {
                updateValueToUi(it)
            }
        }
    }

    private fun updateValueToUi(data: JobListUiData) {
        binding.clRoot.isVisible = data.isInitialized
        adapter?.submitList(data.jobList)
        binding.incUserHeader.tvUserName.text = data.userName ?: getString(R.string.sign_in)
    }
}

data class JobListUiData(
    val isInitialized: Boolean = false,
    val jobList: List<ActiveJobsQuery.Job> = listOf(),
    val userName: String? = null,
    val isUserSignedIn: Boolean = false
)