package com.example.fitnessapp.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.fitnessapp.R
import com.example.fitnessapp.SharedViewModel
import com.example.fitnessapp.data.GymExercise
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {


    private val navController: NavController by lazy {
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.fragment_container) as NavHostFragment
        navHostFragment.navController
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val bottomNavigationBar = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        val actionBar: ActionBar? = supportActionBar
        actionBar?.hide()


        bottomNavigationBar.setupWithNavController(navController)

        val viewModel: SharedViewModel = ViewModelProvider(this)[SharedViewModel::class.java]
        viewModel.fetchData()


        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id in listOf(
                    R.id.homeFragment,
                    R.id.sessionFragment,
                    R.id.workoutsFragment,
                    R.id.profileFragment
                )
            ) {
                bottomNavigationBar.visibility = View.VISIBLE
            } else {
                bottomNavigationBar.visibility = View.GONE
            }
        }


        fun switchToDetail(fragment: Fragment, data: GymExercise) {
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.fragment_container, fragment)
                commit()
            }
        }


    }
}


