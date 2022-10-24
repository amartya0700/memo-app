package com.publicprojects.memo.util

import android.app.Activity
import android.os.SystemClock
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import java.text.SimpleDateFormat
import java.util.*

/**
 * prevent view to be accidentally clicked twice within a second
 */
inline fun View.clickWithDebounce(debounceTime: Long = 1000L, crossinline action: (View) -> Unit) {
    this.setOnClickListener(object : View.OnClickListener {
        private var lastClick = 0L
        override fun onClick(v: View) {
            if (SystemClock.elapsedRealtime() - lastClick < debounceTime)
                return
            action(v)
            lastClick = SystemClock.elapsedRealtime()
        }
    })
}

/**
 * navigate to another fragment using `NavDirections`
 */
fun View.navigate(directions: NavDirections) {
    findNavController().navigate(directions)
}

fun View.enabled(flag: Boolean) {
    if (flag) {
        alpha = 1f
        isClickable = true
    } else {
        alpha = 0.5f
        isClickable = false
    }
}

fun View.show() {
    isVisible = true
}

fun View.hide() {
    isVisible = false
}

fun Activity.setToolbarBackButton(toolbar: Toolbar) {
    (this as AppCompatActivity).apply {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }
    toolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }
}

inline fun EditText.afterTextChange(crossinline action: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            //
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            //
        }

        override fun afterTextChanged(s: Editable?) {
            s?.let { action(it.toString()) }
        }
    })
}

infix fun String.isAfter(end: String): Boolean {
    if (this.isBlank() || end.isBlank())    return false
    val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
    val startT = sdf.parse(this)
    val endT = sdf.parse(end)
    return startT?.let { it > endT } ?: false
}
