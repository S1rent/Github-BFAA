package com.philipprayitno.github.repository

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.database.MatrixCursor
import android.net.Uri
import com.philipprayitno.github.model.RealmUserModel
import com.philipprayitno.github.repository.DatabaseContract.AUTHORITY
import io.realm.Realm
import io.realm.RealmResults

class GithubProvider : ContentProvider() {

    companion object {
        private const val USER = 1
        private val sUriMatcher = UriMatcher(UriMatcher.NO_MATCH)
        private lateinit var realm: Realm

        init {
            sUriMatcher.addURI(AUTHORITY, "RealmUserModel", USER)
        }
    }

    override fun onCreate(): Boolean {
        Realm.init(context)
        realm = Realm.getDefaultInstance()
        return true
    }
    override fun query(
        uri: Uri,
        projection: Array<String>?,
        selection: String?,
        selectionArgs: Array<String>?,
        sortOrder: String?
    ): Cursor {
        val match = sUriMatcher.match(uri)

        val realm = Realm.getDefaultInstance()
        val cursor = MatrixCursor(
            arrayOf(
                "username",
                "fullname",
                "avatar"
            )
        )

        try {
            when (match) {
                USER -> {
                    val results: RealmResults<RealmUserModel> =
                        realm.where(RealmUserModel::class.java).findAll()
                    for (user in results) {
                        val rowData = arrayOf<Any>(
                            user.username ?: "-",
                            user.fullname ?: "-",
                            user.avatar ?: "-"
                        )
                        cursor.addRow(rowData)
                    }
                }
                else -> throw UnsupportedOperationException("Unknown uri: $uri")
            }
            cursor.setNotificationUri(context!!.contentResolver, DatabaseContract.GITHUB_URI)
        } finally {
            realm.close()
        }
        return cursor
    }

    override fun getType(uri: Uri): String? {
        return null
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        return null
    }
    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        return 0
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        return 0
    }
}