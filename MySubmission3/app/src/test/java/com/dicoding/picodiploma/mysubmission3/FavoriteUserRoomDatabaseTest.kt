
package com.dicoding.picodiploma.mysubmission3

import android.content.Context
import android.os.Build.VERSION_CODES.Q
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.dicoding.picodiploma.mysubmission3.database.FavoriteUser
import com.dicoding.picodiploma.mysubmission3.database.FavoriteUserDao
import com.dicoding.picodiploma.mysubmission3.database.FavoriteUserRoomDatabase
import org.junit.Assert.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import java.io.IOException
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Q])
class FavoriteUserRoomDatabaseTest {

    private lateinit var favoriteUserDao: FavoriteUserDao
    private lateinit var db: FavoriteUserRoomDatabase
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, FavoriteUserRoomDatabase::class.java).build()
        favoriteUserDao = db.favoriteUserDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    fun testExistsFavoriteUser() = executorService.execute {
        val favoriteUser = FavoriteUser(46712776L, "oni4hmad", "https://avatars.githubusercontent.com/u/46712776?v=4")
        favoriteUserDao.insert(favoriteUser)
        val isFavoriteUserExist = favoriteUserDao.isUserFavorite("oni4hmad").value as Boolean
        assertEquals(true, isFavoriteUserExist)
    }

    @Test
    fun testNotExistsFavoriteUser() = executorService.execute {
        val favoriteUser = FavoriteUser(46712776L, "oni4hmad", "https://avatars.githubusercontent.com/u/46712776?v=4")
        favoriteUserDao.delete(favoriteUser)
        val isFavoriteUserExist = favoriteUserDao.isUserFavorite("oni4hmad")
        assertEquals(false, isFavoriteUserExist)
    }

}