package com.dicoding.picodiploma.mysubmission2

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    var username: String,
    var name: String,
    var location: String,
    var company: String,
    var avatar: Int,
    var following: String,
    var followers: String,
    var repository: String
) : Parcelable
