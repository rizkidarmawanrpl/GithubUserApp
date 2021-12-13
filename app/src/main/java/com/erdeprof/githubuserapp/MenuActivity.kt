package com.erdeprof.githubuserapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MenuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        setTitle(R.string.app_name_settings)
    }
}