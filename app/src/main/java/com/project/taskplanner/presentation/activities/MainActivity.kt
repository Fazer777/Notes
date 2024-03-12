package com.project.taskplanner.presentation.activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.project.taskplanner.R


class MainActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar;
    private lateinit var bottomNavBar : BottomNavigationView;
    private lateinit var navController : NavController;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        initWidgets()

        setSupportActionBar(toolbar);
        supportActionBar?.setDisplayShowTitleEnabled(false)

        NavigationUI.setupWithNavController(bottomNavBar, navController )
    }

    private fun initWidgets() {
        bottomNavBar = findViewById<BottomNavigationView>(R.id.id_bottom_nav_menu)
        navController = findNavController(R.id.id_nav_host_fragment)
        toolbar = findViewById(R.id.main_act_toolbar)
    }
}