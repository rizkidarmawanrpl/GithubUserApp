package com.erdeprof.githubuserapp.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.erdeprof.githubuserapp.database.*
import com.erdeprof.githubuserapp.helper.Event
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {
    private val _itemsItem = MutableLiveData<List<ItemsItem>>()
    val itemsItem: LiveData<List<ItemsItem>> = _itemsItem

    private val _userDetail = MutableLiveData<UserDetailResponse>()
    val userDetail: LiveData<UserDetailResponse> = _userDetail

    private val _userFollower = MutableLiveData<List<UserFollowerResponseItem>>()
    val userFollower: LiveData<List<UserFollowerResponseItem>> = _userFollower

    private val _userFollowing = MutableLiveData<List<UserFollowerResponseItem>>()
    val userFollowing: LiveData<List<UserFollowerResponseItem>> = _userFollowing

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val statusMessage = MutableLiveData<Event<String>>()

    val message : LiveData<Event<String>>
        get() = statusMessage

    fun getSearchUser(username: String) {
        if (username?.length >= 1) _isLoading.value = true
        if (username?.length >= 4) {
            val client = ApiConfig.getApiService().getSearchUsers(username)
            client.enqueue(object : Callback<UserSearchResponse> {
                override fun onResponse(
                    call: Call<UserSearchResponse>,
                    response: Response<UserSearchResponse>
                ) {
                    _isLoading.value = false
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        if (responseBody != null) {
                            _itemsItem.value = responseBody?.items
                        } else {
                            statusMessage.value = Event("Data pencarian user gagal dimuat!")
                        }
                    } else {
                        statusMessage.value = Event("Data pencarian user gagal dimuat!")
                        Log.e(TAG, "onFailure: ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<UserSearchResponse>, t: Throwable) {
                    _isLoading.value = false
                    statusMessage.value = Event("Data pencarian user gagal dimuat!")
                    Log.e(TAG, "onFailure: ${t.message}")
                }
            })
        }
    }

    fun getDetailUser(username: String) {
        val client = ApiConfig.getApiService().getDetailUsers(username)
        client.enqueue(object : Callback<UserDetailResponse> {
            override fun onResponse(
                call: Call<UserDetailResponse>,
                response: Response<UserDetailResponse>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _userDetail.value = responseBody
                    } else {
                        statusMessage.value = Event("Data user gagal dimuat!")
                    }
                } else {
                    statusMessage.value = Event("Data user gagal dimuat!")
                    Log.e(TAG, "onFailure1: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<UserDetailResponse>, t: Throwable) {
                statusMessage.value = Event("Data user gagal dimuat!")
                Log.e(TAG, "onFailure2: ${t.message}")
            }
        })
    }

    fun getFollowerUser(username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getFollowersUsers(username)
        client.enqueue(object : Callback<List<UserFollowerResponseItem>> {
            override fun onResponse(
                call: Call<List<UserFollowerResponseItem>>,
                response: Response<List<UserFollowerResponseItem>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _userFollower.value = responseBody
                    } else {
                        statusMessage.value = Event("Data follower user gagal dimuat!")
                    }
                } else {
                    statusMessage.value = Event("Data follower user gagal dimuat!")
                    Log.e(TAG, "onFailure1: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<UserFollowerResponseItem>>, t: Throwable) {
                _isLoading.value = false
                statusMessage.value = Event("Data follower user gagal dimuat!")
                Log.e(TAG, "onFailure2: ${t.message}")
            }
        })
    }

    fun getFollowingUser(username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getFollowingUsers(username)
        client.enqueue(object : Callback<List<UserFollowerResponseItem>> {
            override fun onResponse(
                call: Call<List<UserFollowerResponseItem>>,
                response: Response<List<UserFollowerResponseItem>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _userFollowing.value = responseBody
                    } else {
                        statusMessage.value = Event("Data following user gagal dimuat!")
                    }
                } else {
                    statusMessage.value = Event("Data following user gagal dimuat!")
                    Log.e(TAG, "onFailure1: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<UserFollowerResponseItem>>, t: Throwable) {
                _isLoading.value = false
                statusMessage.value = Event("Data following user gagal dimuat!")
                Log.e(TAG, "onFailure2: ${t.message}")
            }
        })
    }

    companion object {
        private const val TAG = "MainViewModel"
    }
}