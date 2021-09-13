package com.philipprayitno.github.model

import com.google.gson.annotations.SerializedName

data class UserDetailResponseWrapper(
    @SerializedName("login")
    val username: String?,
    @SerializedName("avatar_url")
    val avatar: String?,
    val name: String?,
    val location: String?,
    @SerializedName("public_repos")
    val repository: Int?,
    val company: String?,
    val following: Int?,
    val followers: Int?
)
