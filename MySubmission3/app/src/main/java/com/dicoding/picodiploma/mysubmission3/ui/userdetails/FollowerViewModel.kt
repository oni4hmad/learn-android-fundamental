package com.dicoding.picodiploma.mysubmission3.ui.userdetails

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.picodiploma.mysubmission3.network.ApiConfig
import com.dicoding.picodiploma.mysubmission3.network.UserFollower
import com.dicoding.picodiploma.mysubmission3.ui.Event
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

    private val _snackbarText = MutableLiveData<Event<String>>()
    val snackbarText: LiveData<Event<String>> = _snackbarText

    val isResultEmpty: Boolean get() = userFollowers.value?.isEmpty() ?: true

    private val _toastText = MutableLiveData<Event<String>>()
    val toastText: LiveData<Event<String>> = _toastText

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
                    if (isResultEmpty) _toastText.value = Event("User tidak mempunyai follower")
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                    _snackbarText.value = Event(response.message())
                }
            }
            override fun onFailure(call: Call<List<UserFollower>>, t: Throwable) {
                _isLoading.value = false
                _snackbarText.value = Event(t.message.toString())
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }
}