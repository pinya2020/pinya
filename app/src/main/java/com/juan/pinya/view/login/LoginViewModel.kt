package com.juan.pinya.view.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.juan.pinya.model.SingleLiveEvent
import com.juan.pinya.module.SharedPreferencesManager
import com.juan.pinya.module.StuffDao
import kotlinx.coroutines.launch

class LoginViewModel(
    private val stuffDao: StuffDao,
    private val sharedPreferencesManager: SharedPreferencesManager
) : ViewModel() {

    private val mIsLoginSuccess = SingleLiveEvent<Boolean>()
    val isLoginSuccess: LiveData<Boolean>
        get() = mIsLoginSuccess

    private val mError = SingleLiveEvent<Throwable>()
    val error: LiveData<Throwable>
        get() = mError

    fun doLoginAction(id: String, password: String) {
        viewModelScope.launch {
            stuffDao.getStuffById(id).onSuccess { stuff ->
                if (stuff != null) {
                    if (stuff.password == password) {
                        sharedPreferencesManager.id = stuff.id
                    }
                    mIsLoginSuccess.setValue(stuff.password == password)
                }
            }.onFailure {
                mError.setValue(it)
            }
        }
    }
}