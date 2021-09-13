package com.philipprayitno.githubconsumerapp.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class RealmUserModel(
    @PrimaryKey
    var username: String? = "",
    var fullname: String? = "",
    var avatar: String? = "",
): RealmObject()