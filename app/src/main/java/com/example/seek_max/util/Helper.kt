package com.example.seek_max.util

import android.content.Context
import android.os.Build
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import com.example.seek_max.R
import com.example.seek_max.SeekMaxApplication
import com.google.android.material.snackbar.Snackbar
import java.text.NumberFormat
import java.util.*

fun showToast(aUiMessage: AndroidUiMessage) {
    val toast =
        Toast.makeText(SeekMaxApplication.getContext(), aUiMessage.getMessageText(), Toast.LENGTH_LONG)
    if (Build.VERSION.SDK_INT < 30) {
        val textView = toast.view?.findViewById(android.R.id.message) as? TextView
        if (textView != null) {
            textView.gravity = Gravity.CENTER
            textView.setPadding(20, 0, 20, 0)
        }
    }
    toast.show()
}

fun showSnackBarMessage(
    context: Context, coordinator: CoordinatorLayout, aUiMessage: AndroidUiMessage
) = Snackbar.make(coordinator, aUiMessage.getMessageText(), Snackbar.LENGTH_LONG).apply {
        val textColor = ContextCompat.getColor(context, R.color.white)
        val bgColor = ContextCompat.getColor(
            context, if (aUiMessage.isError) R.color.saRed
            else R.color.saGreen2
        )
        val textView = view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
        textView.maxLines = 5
        val buttonView = view.findViewById<Button>(com.google.android.material.R.id.snackbar_action)
        view.setBackgroundColor(bgColor)
        textView.setTextColor(textColor)
        buttonView.setTextColor(textColor)
        show()
    }

fun View.setOnSingleClickListener(onSafeClick: (View) -> Unit) {
    val safeClickListener = OnSingleClickListener(
        onSafeClick = onSafeClick
    )
    setOnClickListener(safeClickListener)
}

inline fun <T> tryOrNull(f: () -> T?) =
    try {
        f()
    } catch (error: Exception) {
        Log.v("SeekMax", error.toString(), error)
        null
    }

fun Int?.toRinggitWithDecimal(): String {
    this ?: return ""
    val doubleVal = this.toDouble()

    if (doubleVal == 0.0)
        return "RM 0.00"

    val formatter = NumberFormat.getInstance(Locale.US)
    formatter.maximumFractionDigits = 2
    formatter.minimumFractionDigits = 2
    return if (doubleVal < 0) {
            "-RM ${formatter.format(doubleVal * -1)}"
        } else {
            "RM ${formatter.format(doubleVal)}"
        }
}