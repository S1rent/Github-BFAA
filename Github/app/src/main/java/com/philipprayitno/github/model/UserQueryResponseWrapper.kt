package com.philipprayitno.github.model

import com.google.gson.annotations.SerializedName

data class UserQueryResponseWrapper(
    @SerializedName("items")
    val data: ArrayList<UserQueryResults>?
)

data class UserQueryResults(
    @SerializedName("login")
    val username: String?
)
