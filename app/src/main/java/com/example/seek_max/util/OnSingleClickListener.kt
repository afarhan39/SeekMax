package com.example.seek_max.util

import android.os.SystemClock
import android.view.View

class OnSingleClickListener(
    private val onSafeClick: (View) -> Unit,
    private var defaultInterval: Int = 500
    ) : View.OnClickListener {
    private var lastTimeClicked: Long = 0
    override fun onClick(v: View) {
        if (SystemClock.elapsedRealtime() - lastTimeClicked < defaultInterval) {
            return
        }
        lastTimeClicked = SystemClock.elapsedRealtime()
        onSafeClick(v)
    }
}