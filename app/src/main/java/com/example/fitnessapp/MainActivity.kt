package com.example.fitnessapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils.replace
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.navigation.fragment.findNavController



class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val homeFragment = HomeFragment()
        val profileFragment = ProfileFragment()
        val sessionFragment = SessionFragment()
        val workoutsFragment = WorkoutsFragment()
        val bottomNavigationBar = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragment_container) as? NavHostFragment
        if (navHostFragment != null) {
            val navController = navHostFragment.navController
            val bottomNavigationBar = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        }

        setCurrentFragment(homeFragment)

        bottomNavigationBar.setOnItemSelectedListener {
            when(it.itemId){
                R.id.itHome -> setCurrentFragment(homeFragment)
                R.id.itSession ->setCurrentFragment(sessionFragment)
                R.id.itWorkouts -> setCurrentFragment(workoutsFragment)
                R.id.itProfile ->setCurrentFragment(profileFragment)
            }
            true
        }

      }

    private fun setCurrentFragment(fragment: Fragment)=
        supportFragmentManager.beginTransaction().apply{
            replace(R.id.fragment_container, fragment)
            commit()
        }
}


