package com.philipprayitno.github.helper

import android.database.Cursor
import com.philipprayitno.github.model.RealmUserModel
import com.philipprayitno.github.model.User
import com.philipprayitno.github.model.UserAvatar
import com.philipprayitno.github.model.UserDetailResponseWrapper
import com.philipprayitno.github.repository.DatabaseContract

class Helper {
    companion object {
        fun mapUserDetailResponseWrapperToUser(detailData: UserDetailResponseWrapper): User {
            return User(
                detailData.username ?: "-",
                detailData.name ?: "-",
                detailData.location ?: "-",
                detailData.repository.toString(),
                detailData.company ?: "-",
                detailData.following.toString(),
                detailData.followers.toString(),
                UserAvatar(-1, detailData.avatar ?: "-")
            )
        }

        fun mapUserToRealmModel(user: User?): RealmUserModel {
            return RealmUserModel(
                user?.userName ?: "-",
                user?.name ?: "-",
                user?.avatar?.stringAvatar ?: ""
            )
        }
    }
}