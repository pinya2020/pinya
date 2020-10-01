package com.juan.pinya.module

import android.content.SharedPreferences

class SharedPreferencesManager(private val sharedPreferences: SharedPreferences) {

    var id: String
        get() = getValue(ID_KEY, "")
        set(value) = setValue(ID_KEY, value)

    var password: String
        get() = getValue(PASSWORD_KEY, "")
        set(value) = setValue(PASSWORD_KEY, value)

    var name: String
        get() = getValue(NAME_KEY,"")
        set(value) = setValue(NAME_KEY,value)

    private fun <T> setValue(key: String, value: T) {
        when (value) {
            is String -> {
                sharedPreferences.edit().putString(key, value).apply()
            }
            is Int -> {
                sharedPreferences.edit().putInt(key, value).apply()
            }
        }
    }

    private inline fun <reified T> getValue(key: String, defValue: T): T {
        val value = when (defValue) {
            is String -> {
                sharedPreferences.getString(key, defValue) as? T
            }
            is Int -> {
                sharedPreferences.getInt(key, defValue) as? T
            }
            else -> {
                defValue
            }
        }
        return value ?: defValue
    }

    companion object {
        private const val ID_KEY = "id"
        private const val PASSWORD_KEY = "password"
        private const val NAME_KEY = "name"
    }
}