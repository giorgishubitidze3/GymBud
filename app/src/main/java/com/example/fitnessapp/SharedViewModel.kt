package com.example.fitnessapp

import androidx.lifecycle.ViewModel

class SharedViewModel: ViewModel() {
    private var selectedNavItem = R.id.itHome
    fun getSelectedNavItem(): Int{
        return selectedNavItem
    }
    fun setSelectedNavItem(itemId: Int){
        selectedNavItem = itemId
    }

    fun navigateTo(itemId: Int){
        selectedNavItem = itemId

    }
}