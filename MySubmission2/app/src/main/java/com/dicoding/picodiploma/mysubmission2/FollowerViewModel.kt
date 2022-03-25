package com.dicoding.picodiploma.mysubmission2

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowerViewModelFactory(private val username: String): ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T = FollowerViewModel(username) as T
}

class FollowerViewModel(val username: String) : ViewModel() {

    companion object {
        private const val TAG = "FollowerViewModel"
    }

    private val _userFollowers = MutableLiveData<List<UserFollower>>()
    val userFollowers: LiveData<List<UserFollower>> = _userFollowers

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        findUserFollowers(username)
    }

    private fun findUserFollowers(username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getUserFollowers(username)
        client.enqueue(object : Callback<List<UserFollower>> {
            override fun onResponse(
                call: Call<List<UserFollower>>,
                response: Response<List<UserFollower>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _userFollowers.value = response.body()
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<List<UserFollower>>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }
}