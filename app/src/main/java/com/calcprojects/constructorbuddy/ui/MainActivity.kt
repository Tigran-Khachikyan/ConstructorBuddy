package com.calcprojects.constructorbuddy.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.preference.PreferenceManager
import com.calcprojects.constructorbuddy.R
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {

    lateinit var bottomNavView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNavView = findViewById(R.id.nav_view)
        bottomNavView.setupWithNavController(findNavController(R.id.nav_host_fragment))
        PreferenceManager.setDefaultValues(this, R.xml.preference, false)
    }
}
