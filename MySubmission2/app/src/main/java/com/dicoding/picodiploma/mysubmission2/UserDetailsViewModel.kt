package com.dicoding.picodiploma.mysubmission2

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserDetailsViewModelFactory(private val username: String): ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T = UserDetailsViewModel(username) as T
}

class UserDetailsViewModel(val username: String) : ViewModel() {

    companion object {
        private const val TAG = "UserDetailsViewModel"
    }

    private val _userInfo = MutableLiveData<UserInfo>()
    val userInfo: LiveData<UserInfo> = _userInfo

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        findUserInfo(username)
    }

    private fun findUserInfo(username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getUserInfo(username)
        client.enqueue(object : Callback<UserInfo> {
            override fun onResponse(
                call: Call<UserInfo>,
                response: Response<UserInfo>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _userInfo.value = response.body()
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<UserInfo>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

}