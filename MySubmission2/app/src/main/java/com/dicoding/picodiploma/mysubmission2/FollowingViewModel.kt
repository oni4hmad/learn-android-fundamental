package com.dicoding.picodiploma.mysubmission2

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class FollowingViewModelFactory(private val username: String): ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T = FollowingViewModel(username) as T
}

class FollowingViewModel(val username: String) : ViewModel() {

    companion object {
        private const val TAG = "FollowingViewModel"
    }

    private val _userFollowing = MutableLiveData<List<UserFollowing>>()
    val userFollowing: LiveData<List<UserFollowing>> = _userFollowing

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        findUserFollowing(username)
    }

    private fun findUserFollowing(username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getUserFollowing(username)
        client.enqueue(object : Callback<List<UserFollowing>> {
            override fun onResponse(
                call: Call<List<UserFollowing>>,
                response: Response<List<UserFollowing>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _userFollowing.value = response.body()
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<List<UserFollowing>>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }
}