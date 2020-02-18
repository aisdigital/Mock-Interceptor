package com.aisdigital.retrofit2.mock.example.base

import android.app.Activity
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.aisdigital.retrofit2.mock.example.R
import com.google.android.material.snackbar.Snackbar

open class BaseActivity : AppCompatActivity() {

    fun showSnackBarMessage(
        view: View,
        message: String,
        onRetry: (() -> Unit)? = null,
        onDismissed: (() -> Unit)? = null,
        duration: Int = Snackbar.LENGTH_LONG
    ) {
        message.let {
            val mySnackBar = Snackbar.make(view, it, duration)
            onRetry?.let { retryListener ->
                mySnackBar.setAction(R.string.retrofit_server_error_retry_btn) {
                    retryListener.invoke()
                }
            }
            mySnackBar.show()
            mySnackBar.addCallback(object : Snackbar.Callback() {
                override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                    onDismissed?.let { listener -> listener() }
                }
            })
        }
    }

    open fun showLoading() {}
    open fun hideLoading() {}
}