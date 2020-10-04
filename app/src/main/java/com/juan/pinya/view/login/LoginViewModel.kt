package com.juan.pinya.view.login

import android.app.AlertDialog
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.juan.pinya.model.LoginType
import com.juan.pinya.model.SingleLiveEvent
import com.juan.pinya.module.SharedPreferencesManager
import com.juan.pinya.module.StuffDao
import kotlinx.coroutines.launch

class LoginViewModel(
    private val stuffDao: StuffDao,
    private val sharedPreferencesManager: SharedPreferencesManager

) : ViewModel() {

    private val mIsLoginSuccess = SingleLiveEvent<LoginType>()
    val isLoginSuccess: LiveData<LoginType>
        get() = mIsLoginSuccess

    private val mError = SingleLiveEvent<Throwable>()
    val error: LiveData<Throwable>
        get() = mError

    fun autoLogin() {
        if (
            sharedPreferencesManager.id.isNotEmpty()
            && sharedPreferencesManager.password.isNotEmpty()
        ) {
            doLoginAction(
                sharedPreferencesManager.id,
                sharedPreferencesManager.password,
                LoginType.AutoLogin(false)
            )
        }
    }

    fun doLoginAction(id: String, password: String, loginType: LoginType) {
        viewModelScope.launch {
            stuffDao.getStuffById(id).onSuccess { stuff ->
                if (stuff?.password == password) {
                    sharedPreferencesManager.id = stuff.id
                    sharedPreferencesManager.password = stuff.password
                    sharedPreferencesManager.name = stuff.name
                }
                loginType.isLoginSuccess = stuff?.password == password
                mIsLoginSuccess.setValue(loginType)
            }.onFailure {
                mError.setValue(it)
            }
        }
    }
}