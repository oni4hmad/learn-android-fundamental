package com.dicoding.picodiploma.mysharedpreferences

import android.content.Context

/**
 * memanipulasi obyek Shared Preferences
 * menambah nilai data ketika belum ada atau mengubah data.
 */
internal class UserPreference(context: Context) {

    /* key */
    companion object {
        private const val PREFS_NAME = "user_pref"
        private const val NAME = "name"
        private const val EMAIL = "email"
        private const val AGE = "age"
        private const val PHONE_NUMBER = "phone"
        private const val LOVE_MU = "islove"
    }

    /* membuat shared preferences */
    private val preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    /* menyimpan data ke shared preferences */
    fun setUser(value: UserModel) {
        val editor = preferences.edit()
        editor.putString(NAME, value.name)
        editor.putString(EMAIL, value.email)
        editor.putInt(AGE, value.age)
        editor.putString(PHONE_NUMBER, value.phoneNumber)
        editor.putBoolean(LOVE_MU, value.isLove)
        editor.apply()  /* simpan: Apply dijalankan secara asynchronous, sedangkan Commit secara synchronous. */
    }

    /* membaca data dari shared preferences */
    fun getUser(): UserModel {
        val model = UserModel()
        model.name = preferences.getString(NAME, "") /* "" -> default value */
        model.email = preferences.getString(EMAIL, "")
        model.age = preferences.getInt(AGE, 0)
        model.phoneNumber = preferences.getString(PHONE_NUMBER, "")
        model.isLove = preferences.getBoolean(LOVE_MU, false)
        return model
    }
}