package com.dicoding.picodiploma.mysubmission2

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {
    /*val userFounded = ArrayList<User>()
    var lastQuery = String()
    val isLasyQueryEmpty: Boolean
        get() = lastQuery.isEmpty()*/

    companion object {
        private const val TAG = "MainViewModel"
    }

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _listUsers = MutableLiveData<List<UserResult>>()
    val listUsers: LiveData<List<UserResult>> = _listUsers

    val isResultEmpty: Boolean get() = listUsers.value?.isEmpty() ?: true
    var lastQuery = String()

    init {
        _isLoading.value = false
    }

    fun searchUser(query: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getUsersSearch(query)
        client.enqueue(object : Callback<SearchResponse> {
            override fun onResponse(
                call: Call<SearchResponse>,
                response: Response<SearchResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    /*val responseBody = response.body()
                    if (responseBody != null) {
                        setRestaurantData(responseBody.restaurant)
                        setReviewData(responseBody.restaurant.customerReviews)
                    }*/
                    _listUsers.value = response.body()?.usersSearchResult
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }
}
