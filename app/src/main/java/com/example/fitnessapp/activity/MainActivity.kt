package com.example.fitnessapp.activity

import android.os.Bundle
import android.util.Log
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
import com.example.fitnessapp.data.User
import com.example.fitnessapp.data.WorkoutViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase


class MainActivity : AppCompatActivity() {


    private val navController: NavController by lazy {
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.fragment_container) as NavHostFragment
        navHostFragment.navController
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val auth = FirebaseAuth.getInstance()
        val database = FirebaseDatabase.getInstance("https://gymbud-application-default-rtdb.europe-west1.firebasedatabase.app")
        val bottomNavigationBar = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        val actionBar: ActionBar? = supportActionBar
        actionBar?.hide()


        bottomNavigationBar.setupWithNavController(navController)

        val viewModel: SharedViewModel = ViewModelProvider(this)[SharedViewModel::class.java]
        val workoutViewModel: WorkoutViewModel = ViewModelProvider(this)[WorkoutViewModel::class.java]
        viewModel.fetchData()

        val currentUser = auth.currentUser


        val usersRef = database.getReference("users")

    if (currentUser != null) {
        val uId = currentUser.uid

        usersRef.child(uId).get().addOnSuccessListener { dataSnapshot ->
            val user = dataSnapshot.getValue(User::class.java)
            if (user != null) {
                workoutViewModel.updateCurrentUserData(user.username, user.name, user.surname)
                Log.d("FirebaseMainActivity", "User data: $user")
                Log.d("FirebaseMainActivity", "User data123123: $user")
                Log.d("FirebaseMainActivity", "User data123123: ${user.name}")
            } else {
                // Log the snapshot to inspect the data structure
                Log.e("FirebaseMainActivity", "User snapshot is null: $dataSnapshot")
                Log.e("FirebaseMainActivity", "User data not found for uid: $uId")
            }
        }.addOnFailureListener { exception ->
            Log.e("FirebaseMainActivity", "Failed to retrieve user data", exception)
        }
    } else {
        Log.e("FirebaseMainActivity", "No authenticated user found")
    }


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


