package br.com.aisdigital.retrofit2.mock.example.base

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import br.com.aisdigital.retrofit2.mock.example.R
import com.google.android.material.snackbar.Snackbar

open class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupStartAnimationView()
    }

    override fun finish() {
        super.finish()
        setupCloseAnimationView()
    }

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

    protected open fun setupStartAnimationView() {
        overridePendingTransition(R.anim.slide_in_right_to_left, R.anim.slide_out_rigth_to_left)
    }

    protected open fun setupCloseAnimationView() {
        overridePendingTransition(R.anim.slide_in_left_to_right, R.anim.slide_out_left_to_right)
    }
}