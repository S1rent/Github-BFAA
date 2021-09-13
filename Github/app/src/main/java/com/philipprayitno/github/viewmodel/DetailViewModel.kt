package com.philipprayitno.github.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.philipprayitno.github.api.RetrofitUserClient
import com.philipprayitno.github.helper.Helper
import com.philipprayitno.github.model.RealmUserModel
import com.philipprayitno.github.model.User
import com.philipprayitno.github.model.UserResponseWrapper
import io.realm.Realm
import io.realm.RealmResults
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class DetailViewModel: ViewModel() {

    private val user = MutableLiveData<User>()
    private val userFollowingList = MutableLiveData<ArrayList<UserResponseWrapper>>()
    private val userFollowerList = MutableLiveData<ArrayList<UserResponseWrapper>>()

    private val isFavorite = MutableLiveData<Boolean>()

    private val realm = Realm.getDefaultInstance()

    fun setUser(username: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = RetrofitUserClient.instance
                    .getUserDetailByUsername(username)
                user.postValue(Helper.mapUserDetailResponseWrapperToUser(result))
            } catch(e: Exception) {
                Log.d("DetailViewModel", e.message ?: "-")
            }
        }
    }

    fun getUser(): LiveData<User> {
        return this.user
    }

    fun setUserFollowingList(username: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = RetrofitUserClient.instance
                        .getUserFollowingList(username)
                userFollowingList.postValue(result)
            } catch(e: Exception) {
                Log.d("DetailViewModel", e.message ?: "-")
            }
        }
    }

    fun getUserFollowingList(): LiveData<ArrayList<UserResponseWrapper>> {
        return this.userFollowingList
    }

    fun setUserFollowerList(username: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = RetrofitUserClient.instance
                        .getUserFollowerList(username)
                userFollowerList.postValue(result)
            } catch(e: Exception) {
                Log.d("DetailViewModel", e.message ?: "-")
            }
        }
    }

    fun getUserFollowerList(): LiveData<ArrayList<UserResponseWrapper>> {
        return this.userFollowerList
    }

    fun addToFavorite() {
        try {
            realm.beginTransaction()
            realm.copyToRealmOrUpdate(Helper.mapUserToRealmModel(user.value))
            realm.commitTransaction()
        } catch(e: Exception) {
            Log.d("DetailViewModel", e.message ?: "-")
        }
    }

    fun checkIfFavorite(username: String): LiveData<Boolean> {
        val results: RealmResults<RealmUserModel> = realm.where(RealmUserModel::class.java)
            .equalTo("username", username)
            .findAll()
        isFavorite.postValue(if(results.size != 0) { true } else { false })
        return isFavorite
    }

    fun removeFromFavorite(username: String) {
        val results: RealmResults<RealmUserModel> = realm.where(RealmUserModel::class.java)
            .equalTo("username", username)
            .findAll()

        try {
            realm.beginTransaction()
            results.deleteFirstFromRealm()
            realm.commitTransaction()
        } catch(e: Exception) {
            Log.d("DetailViewModel", e.message ?: "-")
        }

        if(results.size != 0) {
            isFavorite.postValue(true)
        } else {
            isFavorite.postValue(false)
        }
    }
}