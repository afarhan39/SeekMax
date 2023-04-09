package com.example.seek_max.ui.login

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.widget.doAfterTextChanged
import com.example.seek_max.base.BaseActivity
import com.example.seek_max.databinding.ActivityLoginBinding
import com.example.seek_max.ui.joblist.JobListActivity
import com.example.seek_max.util.launchAndRepeatWithViewLifecycle
import com.example.seek_max.util.observeCommonUIEvents
import com.example.seek_max.util.setOnSingleClickListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : BaseActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViews()
        setListeners()
        observeData()
    }

    private fun initViews() {
        binding.tilUserName.editText?.doAfterTextChanged {
            viewModel.updateUserName(it.toString())
        }

        binding.tilPassword.editText?.doAfterTextChanged {
            viewModel.updatePassword(it.toString())
        }

    }

    private fun setListeners() {
        binding.ivBack.setOnSingleClickListener {
            onBackPressed()
        }

        binding.bSignIn.setOnSingleClickListener {
            viewModel.login()
        }
    }

    private fun observeData() {
        viewModel.commonEvent.observeCommonUIEvents(this, binding.clRoot)

        launchAndRepeatWithViewLifecycle {
            viewModel.mainDataForUi.collect {
                if (it.isInitialized)
                    updateValueToUi(it)
            }
        }

        launchAndRepeatWithViewLifecycle {
            viewModel.mainUiEvent.collect {
                when (it) {
                    LoginUiEvent.NavToJobList -> {
                        startActivity(Intent(this@LoginActivity, JobListActivity::class.java).apply {
                            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        })
                    }
                }
            }
        }
    }

    private fun updateValueToUi(data: LoginUiData) {
        binding.bSignIn.isEnabled = data.isSignInEnabled
    }
}

data class LoginUiData(
    val isInitialized: Boolean = false,
    val isSignInEnabled: Boolean = false
)

sealed class LoginUiEvent {
    object NavToJobList : LoginUiEvent()
}