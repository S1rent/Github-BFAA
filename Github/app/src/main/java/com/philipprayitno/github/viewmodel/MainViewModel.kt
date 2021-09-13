package com.philipprayitno.github.viewmodel

import android.content.res.Resources
import android.content.res.TypedArray
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.philipprayitno.github.helper.Helper
import com.philipprayitno.github.model.UserQueryResults
import com.philipprayitno.github.model.User
import com.philipprayitno.github.model.UserAvatar
import com.philipprayitno.github.R
import com.philipprayitno.github.api.RetrofitUserClient
import com.philipprayitno.github.repository.DatabaseContract
import com.philipprayitno.github.repository.GithubProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class MainViewModel: ViewModel() {

    private lateinit var usernameList: Array<String>
    private lateinit var nameList: Array<String>
    private lateinit var repositoryList: Array<String>
    private lateinit var locationList: Array<String>
    private lateinit var followingList: Array<String>
    private lateinit var followerList: Array<String>
    private lateinit var companyList: Array<String>
    private lateinit var avatarList: TypedArray

    private var userList = MutableLiveData<ArrayList<User>>()

    fun setUsers(query: String, resources: Resources) {
        if(query.isEmpty() || query == "") {
            setupLocalUserData(resources)
            return
        }
        Log.d("TESTING", "${GithubProvider().query(DatabaseContract.GITHUB_URI, null, null, null, null)}")
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = RetrofitUserClient.instance
                    .getUsersByQuery(query)
                if(result.data?.isEmpty() == true) {
                    userList.postValue(arrayListOf())
                }
                getUserDetailList(result.data ?: arrayListOf())
            } catch(e: Exception) {
                Log.d("MainViewModel", e.message ?: "-")
            }
        }
    }

    fun getUsers(): LiveData<ArrayList<User>> {
        return userList
    }

    private suspend fun getUserDetailList(usernameList: ArrayList<UserQueryResults>) {
        val userResultData = ArrayList<User>()

        if (usernameList.isNotEmpty()) {
            for(data in usernameList) {
                try {
                    val result = RetrofitUserClient.instance.getUserDetailByUsername(data.username ?: "-")

                    val mappedUser = Helper.mapUserDetailResponseWrapperToUser(result)
                    userResultData.add(mappedUser)
                }catch(e: Exception) {
                    Log.d("MainViewModel", e.message ?: "-")
                }
            }

            userList.postValue(userResultData)
        }
    }

    private fun setupLocalUserData(resources: Resources) {
        prepareData(resources)
        addData()
    }

    private fun prepareData(resources: Resources) {
        usernameList = resources.getStringArray(R.array.username)
        nameList = resources.getStringArray(R.array.name)
        repositoryList = resources.getStringArray(R.array.repository)
        locationList = resources.getStringArray(R.array.location)
        followerList = resources.getStringArray(R.array.followers)
        followingList = resources.getStringArray(R.array.following)
        companyList = resources.getStringArray(R.array.company)
        avatarList = resources.obtainTypedArray(R.array.avatar)
    }

    private fun addData() {
        val temporaryUserList = ArrayList<User>()
        for(index in usernameList.indices) {
            val user = User(usernameList[index], nameList[index], locationList[index], repositoryList[index],
                companyList[index], followingList[index], followerList[index], UserAvatar(avatarList.getResourceId(index, -1), ""))
            temporaryUserList.add(user)
        }

        userList.postValue(temporaryUserList)
    }
}