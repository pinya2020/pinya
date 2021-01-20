package com.juan.pinya.view.login

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.juan.pinya.R
import com.juan.pinya.di.SHARED_PREFERENCES_NAME
import com.juan.pinya.extention.showAlertDialog
import com.juan.pinya.extention.showLoadingDialog
import com.juan.pinya.extention.showToast
import com.juan.pinya.model.LoginType
import com.juan.pinya.module.SharedPreferencesManager
import kotlinx.android.synthetic.main.activity_login.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel

class LoginActivity : AppCompatActivity() {
    private val loginViewModel by viewModel<LoginViewModel>()
    private val sharedPreferencesManager by inject<SharedPreferencesManager>(SHARED_PREFERENCES_NAME)
    private var alertDialog : AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        id_editText.setText(sharedPreferencesManager.stuffId)
        password_editText.setText(sharedPreferencesManager.password)
        initViews()
        setObservers()
    }

    override fun onResume() {
        super.onResume()
        if(isOnline(this)) {
            if (!sharedPreferencesManager.isFirstLogin) {
                alertDialog = showLoadingDialog()
                loginViewModel.autoLogin()
            }
        }else{
            Toast.makeText(this,getString(R.string.text_please_check_internet),Toast.LENGTH_SHORT).show()
        }
    }
//enter登入
    private fun initViews() {
        password_editText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                login()
                true
            } else {
                false
            }
        }
        login_button.setOnClickListener {
            login()
        }
    }

    private fun login() {
        if (isOnline(this)) {
            val id = id_editText.text.toString()
            if (id.isEmpty()) {
                showToast(getString(R.string.text_please_enter_id))
                return
            }
            val password = password_editText.text.toString()
            alertDialog = showLoadingDialog()
            loginViewModel.doLoginAction(id, password, LoginType.Normal(false, false))
        }else{
            Toast.makeText(this,getString(R.string.text_please_check_internet),Toast.LENGTH_SHORT).show()
        }
    }

    private fun setObservers() {
        loginViewModel.isLoginSuccess.observe(this, Observer { loginType ->
            loginType.action(this, alertDialog)
        })

        loginViewModel.error.observe(this, Observer {
            showAlertDialog(it.message ?: "", getString(R.string.text_retry)) { _, _ ->
                login()
            }
        })
    }
}

private fun isOnline(context: Context): Boolean {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    if (connectivityManager != null) {
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                return true
            }
        }
    }
    return false
}
