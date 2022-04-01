package com.dicoding.picodiploma.mysubmission3.ui.favorite

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.picodiploma.mysubmission3.database.FavoriteUser
import com.dicoding.picodiploma.mysubmission3.network.ApiConfig
import com.dicoding.picodiploma.mysubmission3.network.UserFollowing
import com.dicoding.picodiploma.mysubmission3.repository.FavoriteUserRepository
import com.dicoding.picodiploma.mysubmission3.ui.Event
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class FavoriteViewModelFactory(private val application: Application): ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T = FavoriteViewModel(application) as T
}

class FavoriteViewModel(application: Application) : ViewModel() {

    private val mFavoriteUserRepository: FavoriteUserRepository = FavoriteUserRepository(application)

    fun insert(favoriteUser: FavoriteUser) {
        mFavoriteUserRepository.insert(favoriteUser)
    }

    fun update(favoriteUser: FavoriteUser) {
        mFavoriteUserRepository.update(favoriteUser)
    }

    fun delete(favoriteUser: FavoriteUser) {
        mFavoriteUserRepository.delete(favoriteUser)
    }

    fun getAllFavoriteUsers(): LiveData<List<FavoriteUser>> = mFavoriteUserRepository.getAllFavoriteUsers()

}