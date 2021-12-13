package com.erdeprof.githubuserapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowerFragment : Fragment() {
    private lateinit var rvFollower: RecyclerView
    private val listUsers = ArrayList<User>()
    private val list = ArrayList<User>()
    /*private val listUsers : ArrayList<User>
        get() {
            val dataName = resources.getStringArray(R.array.name)
            val dataFollower = resources.getStringArray(R.array.followers)
            val dataFollowing = resources.getStringArray(R.array.following)
            val dataAvatar = resources.obtainTypedArray(R.array.avatar)
            val dataUsername = resources.getStringArray(R.array.username)
            val dataLocation = resources.getStringArray(R.array.location)
            val dataRepository = resources.getStringArray(R.array.repository)
            val dataCompany = resources.getStringArray(R.array.company)
            val listUser = ArrayList<User>()
            for (i in dataName.indices) {
                val user = User(
                    dataName[i],
                    dataFollower[i].toInt(),
                    dataFollowing[i].toInt(),
                    "https://avatars.githubusercontent.com/u/4090245?v=4",
                    dataUsername[i],
                    dataLocation[i],
                    dataRepository[i].toInt(),
                    dataCompany[i]
                )
                listUser.add(user)
            }

            return listUser;
        }*/

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_follower, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvFollower = view.findViewById(R.id.rv_follower)
        rvFollower.setHasFixedSize(true)

        val tvLabel: TextView = view.findViewById(R.id.tv_tab_follower)
        val index = arguments?.getInt(ARG_SECTION_NUMBER, 0)
        val username = arguments?.getString(ARG_SECTION_USERNAME)

        if (savedInstanceState != null) {
            val descFromBundle = savedInstanceState.getString(EXTRA_DESCRIPTION)
            description = descFromBundle
        }
        var categoryName : String = ""
        if (arguments != null) {
            categoryName = arguments?.getString(EXTRA_NAME).toString()
            // tvLabel.text = categoryName
        }

        when (index) {
            0 -> getFollowerUser(view, username.toString())
            1 -> tvLabel.text = getString(R.string.content_tab_text, index) + "Following " + username
            else -> tvLabel.text = getString(R.string.content_tab_text, index) + " Follower " + username
        }

        /*list.addAll(listUsers)
        showRecyclerList()*/
    }

    private fun getFollowerUser(view: View, username: String) {
        showLoading(view, true)
        val client = ApiConfig.getApiService().getFollowersUsers(username)
        client.enqueue(object : Callback<List<UserFollowerResponseItem>> {
            override fun onResponse(
                call: Call<List<UserFollowerResponseItem>>,
                response: Response<List<UserFollowerResponseItem>>
            ) {
                showLoading(view, false)
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        setUserData(responseBody)
                    }
                    // Log.e(TAG, "onSuccess: OK OK ${response}")
                } else {
                    Log.e(TAG, "onFailure1: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<UserFollowerResponseItem>>, t: Throwable) {
                showLoading(view, false)
                Log.e(TAG, "onFailure2: ${t.message}")
            }
        })
    }

    private fun setUserData(items: List<UserFollowerResponseItem>) {
        // val listUser = ArrayList<User>()
        for (item in items) {
            /*val user = User(
                item.login,
                0,
                0,
                item.avatarUrl,
                item.login,
                "-",
                0,
                "-"
            )

            listUser.add(user)*/
            getDetailUser(item.login)
        }

        list.clear()
        list.addAll(listUsers)
        showRecyclerList()
        // listUsers.clear()
    }

    private fun getDetailUser(username: String) {
        val client = ApiConfig.getApiService().getDetailUsers(username)
        client.enqueue(object : Callback<UserDetailResponse> {
            override fun onResponse(
                call: Call<UserDetailResponse>,
                response: Response<UserDetailResponse>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if(responseBody != null) {
                        setDetailUserData(responseBody)
                    }
                } else {
                    Log.e(TAG, "onFailure1: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<UserDetailResponse>, t: Throwable) {
                Log.e(TAG, "onFailure2: ${t.message}")
            }
        })
    }

    private fun setDetailUserData(detail: UserDetailResponse) {
        val user = User(
            detail.name,
            detail.followers,
            detail.following,
            detail.avatarUrl,
            detail.login,
            detail.location,
            detail.publicRepos,
            detail.company
        )

        listUsers.add(user)
    }

    private fun showRecyclerList() {
        rvFollower.layoutManager = LinearLayoutManager(context)
        val listUserAdapter = ListUserAdapter(list)
        rvFollower.adapter = listUserAdapter
    }

    private fun showLoading(view: View, isLoading: Boolean) {
        val progressBar: ProgressBar = view.findViewById(R.id.progressBar)

        if (isLoading) {
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.GONE
        }
    }

    var description: String? = null

    companion object {

        private const val ARG_SECTION_NUMBER = "section_number"
        private const val ARG_SECTION_USERNAME = "section_username"
        var EXTRA_NAME = "extra_name"
        var EXTRA_DESCRIPTION = "extra_description"
        private const val TAG = "DetailUserActivity"

        @JvmStatic
        fun newInstance(index: Int, username: String) =
            FollowerFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_SECTION_NUMBER, index)
                    putString(ARG_SECTION_USERNAME, username)
                }
            }
    }
}