package com.philipprayitno.github.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val userName: String,
    val name: String,
    val location: String,
    val repository: String,
    val company: String,
    val following: String,
    val followers: String,
    val avatar: UserAvatar
):Parcelable

@Parcelize
data class UserAvatar(
    val intAvatar: Int,
    val stringAvatar: String
): Parcelable