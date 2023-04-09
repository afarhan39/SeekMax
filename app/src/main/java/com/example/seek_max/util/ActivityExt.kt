package com.example.seek_max.util

import android.app.Dialog
import android.content.DialogInterface
import android.graphics.drawable.ColorDrawable
import android.view.KeyEvent
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.example.seek_max.base.BaseActivity
import com.example.seek_max.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

/**
 * Launches a new coroutine and repeats `block` every time the Activity's lifecycleOwner
 * is in and out of `minActiveState` lifecycle state.
 */
inline fun AppCompatActivity.launchAndRepeatWithViewLifecycle(
    crossinline block: suspend CoroutineScope.() -> Unit,
) {
    lifecycleScope.launch {
        lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            block()
        }
    }
}

fun SharedFlow<CommonUIEvent>.observeCommonUIEvents(
    activity: BaseActivity,
    coordinator: CoordinatorLayout,
) {
    activity.launchAndRepeatWithViewLifecycle {
        collect {
            when (it) {
                is CommonUIEvent.ShowMainProgress -> {
                    activity.showProgress()
                }
                is CommonUIEvent.HideMainProgress -> {
                    activity.hideProgress()
                }
                is CommonUIEvent.ShowSnackBarMessage -> {
                    showSnackBarMessage(activity, coordinator, it.message)
                }
                is CommonUIEvent.ShowToast -> {
                    showToast(it.message)
                }
            }
        }
    }
}

fun AppCompatActivity.customProgressDialog(): Dialog {
    val progressDialog = Dialog(this)
    progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    progressDialog.setCancelable(false)
    progressDialog.window?.setBackgroundDrawable(ColorDrawable(0))
    progressDialog.setContentView(R.layout.dialog_progress_loading)
    val ivLoading = progressDialog.findViewById<AppCompatImageView>(R.id.ivLoading)
    Glide.with(this).load(R.raw.loading).into(ivLoading)

    progressDialog.setOnKeyListener(DialogInterface.OnKeyListener { _, keyCode, event ->
        if (keyCode == KeyEvent.KEYCODE_BACK && !event.isCanceled) {
            try {
                if (progressDialog.isShowing) {
                    progressDialog.dismiss()
                }
            } catch (e: Exception) { // do nothing
            }
            return@OnKeyListener true
        }
        false
    })
    return progressDialog
}