package com.example.fitnessapp.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.example.fitnessapp.R
import com.example.fitnessapp.databinding.ActivityLoadingBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class LoadingActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityLoadingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoadingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        auth = Firebase.auth

        Handler(Looper.getMainLooper()).postDelayed({
            val user = auth.currentUser

            if(user != null){
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()

            }else{

                val intent = Intent(this, SignInActivity::class.java)
                startActivity(intent)

                finish()
            }


        },1000)



    }
}