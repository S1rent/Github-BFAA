package com.philipprayitno.githubconsumerapp.viewmodel

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.service.notification.Condition.SCHEME
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.philipprayitno.githubconsumerapp.model.User
import com.philipprayitno.githubconsumerapp.model.UserAvatar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel: ViewModel() {

    private var userList = MutableLiveData<ArrayList<User>>()

    fun setUsers(context: Context) {
        val resolver = context.contentResolver
        val uri = Uri.Builder().scheme(SCHEME)
            .authority("com.philipprayitno.github")
            .appendPath("RealmUserModel")
            .build()
        val cursor = resolver.query(uri, null, null, null, null)
        userList.postValue(mapCursorToArrayList(cursor))

    }

    fun getUsers(): LiveData<ArrayList<User>> {
        return userList
    }

    fun mapCursorToArrayList(userCursor: Cursor?): ArrayList<User> {
        val notesList = ArrayList<User>()

        userCursor?.apply {
            while (moveToNext()) {
                val username = getString(getColumnIndexOrThrow("username"))
                val fullname = getString(getColumnIndexOrThrow("fullname"))
                val avatar = getString(getColumnIndexOrThrow("avatar"))
                notesList.add(User(username, fullname, "", "", "", "", "", UserAvatar(-1, avatar)))
            }
        }
        return notesList
    }
}