package com.philipprayitno.github.repository

import android.app.Application
import android.net.Uri
import io.realm.Realm
import io.realm.RealmConfiguration

object DatabaseContract {
    const val AUTHORITY = "com.philipprayitno.github"
    const val SCHEME = "content"
    val GITHUB_URI: Uri = Uri.Builder().scheme(SCHEME)
        .authority(AUTHORITY)
        .appendPath("RealmUserModel")
        .build()

    class RealmRepository: Application() {
        override fun onCreate() {
            super.onCreate()
            Realm.init(this)
            val config = RealmConfiguration.Builder()
                .name("Favorite.db")
                .deleteRealmIfMigrationNeeded()
                .schemaVersion(0)
                .build()

            Realm.setDefaultConfiguration(config)
        }
    }
}