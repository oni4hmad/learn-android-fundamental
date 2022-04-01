package com.dicoding.picodiploma.mysubmission3.ui.userdetails

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.picodiploma.mysubmission3.network.ApiConfig
import com.dicoding.picodiploma.mysubmission3.network.UserFollowing
import com.dicoding.picodiploma.mysubmission3.ui.Event
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

    private val _snackbarText = MutableLiveData<Event<String>>()
    val snackbarText: LiveData<Event<String>> = _snackbarText

    val isResultEmpty: Boolean get() = userFollowing.value?.isEmpty() ?: true

    private val _toastText = MutableLiveData<Event<String>>()
    val toastText: LiveData<Event<String>> = _toastText

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
                    if (isResultEmpty) _toastText.value = Event("User tidak mempunyai following")
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                    _snackbarText.value = Event(response.message())
                }
            }
            override fun onFailure(call: Call<List<UserFollowing>>, t: Throwable) {
                _isLoading.value = false
                _snackbarText.value = Event(t.message.toString())
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }
}