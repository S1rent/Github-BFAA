package com.philipprayitno.github.model

import com.google.gson.annotations.SerializedName

data class UserResponseWrapper(
    @SerializedName("login")
    val username: String?,
    @SerializedName("avatar_url")
    val avatar: String?
)