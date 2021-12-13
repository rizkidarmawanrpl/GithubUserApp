package com.erdeprof.githubuserapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class FollowerFragment : Fragment() {
    private lateinit var rvFollower: RecyclerView
    private val list = ArrayList<User>()

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

        val mainViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(MainViewModel::class.java)

        mainViewModel.isLoading.observe(viewLifecycleOwner, {
            showLoading(view, it)
        })

        mainViewModel.userFollower.observe(viewLifecycleOwner, { userFollower ->
            setUserData(userFollower)
        })

        mainViewModel.userFollowing.observe(viewLifecycleOwner, { userFollowing ->
            setUserData(userFollowing)
        })

        mainViewModel.message.observe(viewLifecycleOwner, {
            it.getContentIfNotHandled()?.let {
                Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            }
        })

        val index = arguments?.getInt(ARG_SECTION_NUMBER, 0)
        val username = arguments?.getString(ARG_SECTION_USERNAME).toString()

        when (index) {
            0 -> mainViewModel.getFollowerUser(username)
            1 -> mainViewModel.getFollowingUser(username)
            else -> mainViewModel.getFollowerUser(username)
        }
    }

    private fun setUserData(items: List<UserFollowerResponseItem>) {
        val listUser = ArrayList<User>()
        for (item in items) {
            val user = User(
                item.avatarUrl,
                item.login,
            )
            listUser.add(user)
        }

        list.clear()
        list.addAll(listUser)
        showRecyclerList()
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

    companion object {
        private const val ARG_SECTION_NUMBER = "section_number"
        private const val ARG_SECTION_USERNAME = "section_username"

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