package com.dicoding.picodiploma.mysubmission3.ui.main

import android.util.Log
import androidx.lifecycle.*
import com.dicoding.picodiploma.mysubmission3.network.ApiConfig
import com.dicoding.picodiploma.mysubmission3.network.SearchResponse
import com.dicoding.picodiploma.mysubmission3.network.UserResult
import com.dicoding.picodiploma.mysubmission3.ui.Event
import com.dicoding.picodiploma.mysubmission3.ui.settings.SettingPreferences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModelFactory(private val pref: SettingPreferences): ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T = MainViewModel(pref) as T
}

class MainViewModel(private val pref: SettingPreferences) : ViewModel() {

    companion object {
        private const val TAG = "MainViewModel"
    }

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _listUsers = MutableLiveData<List<UserResult>>()
    val listUsers: LiveData<List<UserResult>> = _listUsers

    private val _snackbarText = MutableLiveData<Event<String>>()
    val snackbarText: LiveData<Event<String>> = _snackbarText

    val isResultEmpty: Boolean get() = listUsers.value?.isEmpty() ?: true

    private val _toastText = MutableLiveData<Event<String>>()
    val toastText: LiveData<Event<String>> = _toastText

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
                    _listUsers.value = response.body()?.usersSearchResult
                    if (isResultEmpty) _toastText.value = Event("User tidak ditemukan")
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                    _snackbarText.value = Event(response.message())
                }
            }
            override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                _isLoading.value = false
                _snackbarText.value = Event(t.message.toString())
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    fun getThemeSettings(): LiveData<Boolean> {
        return pref.getThemeSetting().asLiveData()
    }
}
