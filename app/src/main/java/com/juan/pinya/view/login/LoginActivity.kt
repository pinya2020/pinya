package com.juan.pinya.view.login

import android.os.Bundle
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.juan.pinya.R
import com.juan.pinya.extention.showAlertDialog
import com.juan.pinya.extention.showLoadingDialog
import com.juan.pinya.extention.showToast
import com.juan.pinya.model.LoginType
import kotlinx.android.synthetic.main.activity_login.*
import org.koin.android.viewmodel.ext.android.viewModel

class LoginActivity : AppCompatActivity() {
    private val loginViewModel by viewModel<LoginViewModel>()

    private var alertDialog : AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        initViews()
        setObservers()
    }

    override fun onResume() {
        super.onResume()
//        alertDialog = showLoadingDialog()
        loginViewModel.autoLogin()
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
        val id = id_editText.text.toString()
        if (id.isEmpty()) {
            showToast(getString(R.string.text_please_enter_id))
            return
        }
        val password = password_editText.text.toString()
        alertDialog = showLoadingDialog()
        loginViewModel.doLoginAction(id, password, LoginType.Normal(false))
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