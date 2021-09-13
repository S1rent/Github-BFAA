package com.philipprayitno.github.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.philipprayitno.github.api.RetrofitUserClient
import com.philipprayitno.github.model.RealmUserModel
import com.philipprayitno.github.model.User
import com.philipprayitno.github.model.UserAvatar
import io.realm.Realm
import io.realm.RealmResults
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class FavoriteViewModel: ViewModel() {
    private var userList = MutableLiveData<ArrayList<User>>()
    private val realm = Realm.getDefaultInstance()

    fun setUsers() {
        try {
            val result = setupRealmFavoriteData()
            userList.postValue(result)
        } catch(e: Exception) {
            Log.d("FavoriteViewModel", e.message ?: "-")
        }
    }

    fun getUsers(): LiveData<ArrayList<User>> {
        return userList
    }

    private fun setupRealmFavoriteData(): ArrayList<User> {
        val results: RealmResults<RealmUserModel> = realm.where(RealmUserModel::class.java)
            .findAll()
        val temporaryArrayList = arrayListOf<User>()
        for (user: RealmUserModel in results) {
            val mappedUser = User(user.username ?: "", user.fullname ?: "", "", "",
                "", "", "", UserAvatar(-1, user.avatar ?: ""))
            temporaryArrayList.add(mappedUser)
        }
        return temporaryArrayList
    }
}