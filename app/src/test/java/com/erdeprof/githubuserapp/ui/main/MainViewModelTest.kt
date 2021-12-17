package com.erdeprof.githubuserapp.ui.main

import androidx.lifecycle.ViewModelProvider
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before

class MainViewModelTest {

    private lateinit var mainViewModel: MainViewModel

    private val username = "sidiqpermana"

    private val dummyItemsItemCount: Int = 2

    @Before
    fun before() {
        // mainViewModel = MainViewModel()
    }

    @Test
    fun getSearchUser() {
        /*mainViewModel = mock(MainViewModel())
        mainViewModel.getSearchUser(username)
        var itemsItemCount = 0
        mainViewModel.itemsItem.observe(this, { itemsItem ->
            itemsItemCount = itemsItem.size
        })
        assertEquals(dummyItemsItemCount, itemsItemCount)*/
    }

    @Test
    fun getDetailUser() {
    }

    @Test
    fun getFollowerUser() {
    }

    @Test
    fun getFollowingUser() {
    }
}