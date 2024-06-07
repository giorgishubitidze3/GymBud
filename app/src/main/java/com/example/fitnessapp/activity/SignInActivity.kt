package com.example.fitnessapp.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import com.example.fitnessapp.R
import com.example.fitnessapp.databinding.ActivitySignInBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class SignInActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivitySignInBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        auth = Firebase.auth

        binding.signUpBtn.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
            finish()
        }


        binding.buttonSignIn.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPass.text.toString()
            if(checkAllFields()){
                auth.signInWithEmailAndPassword(email,password).addOnCompleteListener{
                    if(it.isSuccessful){
                        Toast.makeText(this, "Successfully signed in", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this,MainActivity::class.java)
                        //intent.putExtra("userCurrentMail", email)
                        startActivity(intent)
                        //destroy this activity
                        finish()
                    }else{
                        Log .e("error: signin", it.exception.toString())
                    }
                }
            }
        }
    }

    private fun checkAllFields(): Boolean{
        val email = binding.etEmail.text.toString()

        if(binding.etEmail.text.toString() == ""){
            binding.textInputLayoutEmail.error ="This is a required field"
            return false
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            binding.textInputLayoutEmail.error = "Check email format"
            return false
        }
        if(binding.etPass.text.toString() == ""){
            binding.textInputLayoutPass.error ="This is a required field"
            return false
        }
        if(binding.etPass.length() <= 6){
            binding.textInputLayoutPass.error ="password should be longer than 6 characters"
            return false
        }

        return true
    }
}