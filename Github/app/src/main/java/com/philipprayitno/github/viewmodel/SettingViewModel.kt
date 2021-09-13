package com.philipprayitno.github.viewmodel

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.philipprayitno.github.helper.SharedPreferencesKey
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class SettingViewModel: ViewModel() {

    private val isChecked = MutableLiveData<Boolean>()

    fun fetchSharedPreferenceData(preference: SharedPreferences) {
        val result = preference.getBoolean(SharedPreferencesKey.REMINDER, false)
        this.isChecked.postValue(result)
    }

    fun setIsChecked(preference: SharedPreferences, checkedValue: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val editor = preference.edit()
                editor.apply {
                    putBoolean(SharedPreferencesKey.REMINDER, checkedValue)
                }.apply()
                isChecked.postValue(checkedValue)
            } catch(e: Exception) {
                Log.d("DetailViewModel", e.message ?: "-")
            }
        }
    }

    fun getIsChecked(): LiveData<Boolean> {
        return this.isChecked
    }
}