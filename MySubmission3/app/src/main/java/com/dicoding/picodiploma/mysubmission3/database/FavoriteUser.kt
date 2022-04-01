package com.dicoding.picodiploma.mysubmission3.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class FavoriteUser (
    @PrimaryKey
    @ColumnInfo(name = "id")
    var id: Long = 0,

    @ColumnInfo(name = "login")
    var login: String = "",

    @ColumnInfo(name = "avatar_url")
    var avatarUrl: String = ""
)