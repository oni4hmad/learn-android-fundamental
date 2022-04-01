package com.dicoding.picodiploma.mysubmission3.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface FavoriteUserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(favoriteUser: FavoriteUser)

    @Update
    fun update(favoriteUser: FavoriteUser)

    @Delete
    fun delete(favoriteUser: FavoriteUser)

    @Query("SELECT * from favoriteuser ORDER BY login ASC")
    fun getAllFavoriteUser(): LiveData<List<FavoriteUser>>

    @Query("SELECT EXISTS(SELECT * FROM favoriteuser WHERE login = :username LIMIT 1)")
    fun isUserFavorite(username: String): LiveData<Boolean>
}