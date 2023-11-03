package com.example.fitnessapp

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fitnessapp.data.GymExercise
import com.example.fitnessapp.network.RetrofitInstance
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


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

        val actionBar: ActionBar? = supportActionBar
        actionBar?.hide()

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
                R.id.itProfile -> setCurrentFragment(profileFragment)
            }
            true
        }

      }

    private fun setCurrentFragment(fragment: Fragment)=
        supportFragmentManager.beginTransaction().apply{
            replace(R.id.fragment_container, fragment)
            commit()
        }

    fun switchToDetail(fragment: Fragment,data: GymExercise){
        supportFragmentManager.beginTransaction().apply{
            replace(R.id.fragment_container, fragment)
            commit()
        }
    }



}


