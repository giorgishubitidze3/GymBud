package com.example.fitnessapp.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.fitnessapp.R
import com.example.fitnessapp.activity.SignInActivity
import com.example.fitnessapp.data.WorkoutViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.database

class ProfileFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewModel: WorkoutViewModel = ViewModelProvider(requireActivity()).get(WorkoutViewModel::class.java)
        val navController = activity?.findNavController(R.id.fragment_container)


        auth = Firebase.auth
        val logOutBtn = view.findViewById<ImageButton>(R.id.logout_button)
        val nameTextView = view.findViewById<TextView>(R.id.user_name_profile)
        val usernameTextView = view.findViewById<TextView>(R.id.user_username_profile)
        val userImage = view.findViewById<ImageView>(R.id.user_profile_picture)
        val settingsCardView = view.findViewById<CardView>(R.id.settings_cardview)
        val friendsCardView = view.findViewById<CardView>(R.id.friends_cardview)
        val statsCardView = view.findViewById<CardView>(R.id.stats_cardview)
        val measurementsCardView = view.findViewById<CardView>(R.id.measurements_cardview)
        val challengesCardView = view.findViewById<CardView>(R.id.challenges_cardview)

        viewModel.currentUserFullName.observe(viewLifecycleOwner){
            nameTextView.text = it
        }

        viewModel.currentUserUsername.observe(viewLifecycleOwner){
            usernameTextView.text = "@${it}"
        }

        settingsCardView.setOnClickListener{
            navController?.navigate(R.id.action_profileFragment_to_settingsFragment)
        }

        friendsCardView.setOnClickListener {
         navController?.navigate(R.id.action_profileFragment_to_friendsFragment)
        }

        statsCardView.setOnClickListener {
            navController?.navigate(R.id.action_profileFragment_to_statsFragment)
        }




        logOutBtn.setOnClickListener {
            auth.signOut()

            val intent = Intent(requireContext(), SignInActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }
    }
}
