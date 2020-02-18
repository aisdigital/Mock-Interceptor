package com.aisdigital.retrofit2.mock.example.ui.home.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.aisdigital.retrofit2.mock.example.R
import com.aisdigital.retrofit2.mock.example.base.BaseActivity
import com.aisdigital.retrofit2.mock.example.databinding.ActivityHomeBinding
import com.aisdigital.retrofit2.mock.example.ui.home.viewmodel.HomeViewModel
import com.aisdigital.retrofit2.mock.example.ui.login.view.LoginActivity
import org.koin.android.viewmodel.ext.android.viewModel

class HomeActivity : BaseActivity() {

    companion object {

        fun startActivity(context: Context) {
            val intent = Intent(context, HomeActivity::class.java)
            context.startActivity(intent)
        }
    }

    private val viewModel by viewModel<HomeViewModel>()

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home)

        initObserver()
        initOnClick()
    }

    private fun initOnClick() {
        binding.logout.setOnClickListener {
            requestLogout()
        }
    }

    private fun requestLogout() {
        showLoading()
        viewModel.logout()
    }

    private fun initObserver() {
        viewModel.logoutResult.observe(this, Observer {
            hideLoading()
            finish()
        })
    }

    override fun hideLoading() {
        binding.loading.visibility = View.GONE
    }

    override fun showLoading() {
        binding.loading.visibility = View.VISIBLE
    }
}
