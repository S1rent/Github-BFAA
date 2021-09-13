package com.philipprayitno.github.repository

import android.app.Application
import io.realm.Realm
import io.realm.RealmConfiguration

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